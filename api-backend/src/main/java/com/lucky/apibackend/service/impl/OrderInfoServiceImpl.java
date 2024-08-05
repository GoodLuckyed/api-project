package com.lucky.apibackend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.mapper.OrderInfoMapper;
import com.lucky.apibackend.model.entity.OrderInfo;
import com.lucky.apibackend.model.entity.ProductInfo;
import com.lucky.apibackend.model.enums.OrderStatus;
import com.lucky.apibackend.model.enums.PayType;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.OrderInfoService;
import com.lucky.apibackend.service.ProductInfoService;
import com.lucky.apibackend.utils.OrderNoUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
* @author ccc
* @description 针对表【order_info(订单信息表)】的数据库操作Service实现
* @createDate 2024-07-25 01:32:00
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Resource
    private ProductInfoService productInfoService;

    /**
     * 创建订单
     * @param productId
     * @param paymentType
     * @param loginUser
     * @return
     */
    @Override
    public OrderInfo createOrderInfo(Long productId, String paymentType, UserVo loginUser) {
        //查找已存在但未支付的订单
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getProductId,productId);
        queryWrapper.eq(OrderInfo::getUserId,loginUser.getId());
        queryWrapper.eq(OrderInfo::getStatus, OrderStatus.NOTPAY.getType());
        if (paymentType.equals("ALIPAY")){
            queryWrapper.eq(OrderInfo::getPaymentType,PayType.ALIPAY.getType());
        }else {
            queryWrapper.eq(OrderInfo::getPaymentType,PayType.WXPAY.getType());
        }
        OrderInfo oldOrderInfo = this.getOne(queryWrapper);
        if (oldOrderInfo != null){
            return oldOrderInfo;
        }
        // 获取产品信息
        ProductInfo productInfo = productInfoService.getById(productId);
        if (productInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTitle(productInfo.getName());
        orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
        orderInfo.setUserId(loginUser.getId());
        orderInfo.setProductId(productId);
        orderInfo.setTotal(productInfo.getTotal());
        orderInfo.setStatus(OrderStatus.NOTPAY.getType());
        orderInfo.setAddPoints(productInfo.getAddPoints());
        if (paymentType.equals("ALIPAY")){
            orderInfo.setPaymentType(PayType.ALIPAY.getType());
        }else {
            orderInfo.setPaymentType(PayType.WXPAY.getType());
        }
        // 设置过期时间 当前时间加上五分钟
        Instant instant = Instant.now().plus(Duration.ofMinutes(5));
        Date expirationTime = Date.from(instant);
        orderInfo.setExpirationTime(expirationTime);
        this.save(orderInfo);
        return orderInfo;
    }

    /**
     * 根据订单号获取订单
     * @param outTradeNo
     * @return
     */
    @Override
    public OrderInfo getOrderByOrderNo(String outTradeNo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getOrderNo,outTradeNo);
        OrderInfo orderInfo = this.getOne(queryWrapper);
        return orderInfo;
    }

    /**
     * 根据订单号更新订单状态
     * @param outTradeNo
     * @param orderStatus
     */
    @Override
    public void updateStatusByOrderNo(String outTradeNo, OrderStatus orderStatus) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(orderStatus.getType());
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getOrderNo,outTradeNo);
        this.update(orderInfo,queryWrapper);
    }

    /**
     * 查询创建超过minutes分钟并且未支付的订单
     * @param paymentType
     * @return
     */
    @Override
    public List<OrderInfo> getNoPayOrderByDuration(String paymentType) {
        Instant instant = Instant.now();
        Date date = Date.from(instant);
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getPaymentType,paymentType);
        queryWrapper.eq(OrderInfo::getStatus,OrderStatus.NOTPAY.getType());
        queryWrapper.le(OrderInfo::getExpirationTime,date);
        return this.list(queryWrapper);
    }
}




