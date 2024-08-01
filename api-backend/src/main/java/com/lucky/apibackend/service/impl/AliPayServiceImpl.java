package com.lucky.apibackend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.alipay.PayCreateRequest;
import com.lucky.apibackend.model.entity.OrderInfo;
import com.lucky.apibackend.model.enums.OrderStatus;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.*;
import com.lucky.apibackend.utils.RedissonLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author lucky
 * @date 2024/7/25
 * @description
 */
@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private UserService userService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private CoinActivityService coinActivityService;
    @Resource
    private RedissonLockUtil redissonLockUtil;
    @Resource
    private AlipayClient alipayClient;
    @Resource
    private Environment environment;
    @Resource
    private PaymentInfoService paymentInfoService;

    /**
     * 手机网站支付
     *
     * @param payCreateRequest
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String tradePay(PayCreateRequest payCreateRequest, HttpServletRequest request) {
        // 获取请求参数
        Long productId = payCreateRequest.getProductId();
        String paymentType = payCreateRequest.getPaymentType();
        // 获取登录用户
        UserVo loginUser = userService.getLoginUser(request);
        // 判断购买的是否是充值活动的产品
        coinActivityService.checkBuyCoinActivity(loginUser.getId(), productId);
        try {
            // 创建订单
            OrderInfo orderInfo = redissonLockUtil.redissonDistributedLocks("getOrder" + loginUser.getUserAccount().intern(),
                    () -> orderInfoService.createOrderInfo(productId, paymentType, loginUser));
            // 调用支付宝支付接口，获取支付宝支付表单
            AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
            //异步接收地址，仅支持http/https，公网可访问
            alipayTradeWapPayRequest.setNotifyUrl(environment.getProperty("alipay.notify-url"));
            //同步跳转地址，仅支持http/https
            alipayTradeWapPayRequest.setReturnUrl(environment.getProperty("alipay.return-url"));
            /******必传参数******/
            JSONObject bizContent = new JSONObject();
            //商户订单号，商家自定义，保持唯一性
            bizContent.put("out_trade_no", orderInfo.getOrderNo());
            //支付金额，最小值0.01元
            BigDecimal totalAmount = new BigDecimal(orderInfo.getTotal().toString()).divide(new BigDecimal("100"));
            bizContent.put("total_amount", totalAmount);
            //订单标题，不可使用特殊符号
            bizContent.put("subject", orderInfo.getTitle());
            /******可选参数******/
            //手机网站支付默认传值QUICK_WAP_WAY
            bizContent.put("product_code", "QUICK_WAP_WAY");
            alipayTradeWapPayRequest.setBizContent(bizContent.toString());
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayTradeWapPayRequest, "POST");
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建支付交易失败");
            }
        } catch (AlipayApiException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建支付交易失败");
        }
    }

    /**
     * 支付通知
     *
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String tradeNotify(Map<String, String> params) {
        try {
            // 验签
            String result = "failure";
            result = checkAliPayNotifySign(params, result);

            if (!"success".equals(result)){
                return result;
            }
            // 根据订单号获取订单信息
            String outTradeNo = params.get("out_trade_no");
            OrderInfo orderInfo = orderInfoService.getOrderByOrderNo(outTradeNo);
            // 处理可能发生的重复通知
            if (!orderInfo.getStatus().equals(OrderStatus.NOTPAY.getType())){
                return "success";
            }
            // 处理业务
            // 更改订单状态
            orderInfoService.updateStatusByOrderNo(params.get("out_trade_no"), OrderStatus.SUCCESS);
            // 记录支付信息
            paymentInfoService.createPaymentInfo(params);
            // 更新用户积分
            userService.addWalletBalance(orderInfo.getUserId(),orderInfo.getAddPoints());
            //判断购买的是否是充值活动产品（该产品只能购买一次）
            coinActivityService.saveRechargeActivity(orderInfo);
            return "success";
        } catch (AlipayApiException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }

    private String checkAliPayNotifySign(Map<String, String> params, String result) throws AlipayApiException {
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                environment.getProperty("alipay.alipay-public-key"),
                AlipayConstants.CHARSET_UTF8,
                AlipayConstants.SIGN_TYPE_RSA2); //调用SDK验证签名
        if (!signVerified) {
            log.error("支付宝签名验证失败");
            return result;
        }
        //验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
        // 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure

        // a. 商家需要验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
        String outTradeNo = params.get("out_trade_no");
        OrderInfo orderInfo = orderInfoService.getOrderByOrderNo(outTradeNo);
        if (orderInfo == null){
            log.error("订单不存在，订单号：{}",outTradeNo);
            return result;
        }
        // b.判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）。
        String totalAmount = params.get("total_amount");
        int totalAmountInt  = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();
        int totalFeeInt  = orderInfo.getTotal().intValue();
        if (totalAmountInt != totalFeeInt){
            log.error("订单金额不一致，订单号：{}",outTradeNo);
            return result;
        }
        // c.校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商家可能有多个 seller_id/seller_email）。
        String sellerId = params.get("seller_id");
        String sellerIdProperty  = environment.getProperty("alipay.seller-id");
        if (!sellerId.equals(sellerIdProperty)){
            log.error("商户ID不一致，订单号：{}",outTradeNo);
            return result;
        }
        // d.验证 app_id 是否为该商家本身。
        String appId = params.get("app_id");
        String appIdProperty = environment.getProperty("alipay.app-id");
        if (!appId.equals(appIdProperty)){
            log.error("应用ID不一致，订单号：{}",outTradeNo);
            return result;
        }
        // 状态 TRADE_SUCCESS 的通知触发条件是商家开通的产品支持退款功能的前提下，买家付款成功。
        String tradeStatus = params.get("trade_status");
        if (!"TRADE_SUCCESS".equals(tradeStatus)){
            log.error("交易状态不正确，订单号：{}",outTradeNo);
            return result;
        }

        return "success";
    }

}


























