package com.lucky.apibackend.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lucky.apibackend.mapper.PaymentInfoMapper;
import com.lucky.apibackend.model.entity.PaymentInfo;
import com.lucky.apibackend.model.enums.PayType;
import com.lucky.apibackend.service.PaymentInfoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
* @author ccc
* @description 针对表【payment_info(支付信息表)】的数据库操作Service实现
* @createDate 2024-07-25 01:32:30
*/
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    /**
     * 记录支付信息
     * @param params
     */
    @Override
    public void createPaymentInfo(Map<String, String> params) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(params.get("out_trade_no"));
        paymentInfo.setTransactionId(params.get("trade_no"));
        paymentInfo.setPaymentType(PayType.ALIPAY.getType());
        paymentInfo.setTradeType("电脑网站支付");
        paymentInfo.setTradeState(params.get("trade_status"));
        String totalAmount = params.get("total_amount");
        int total = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();
        paymentInfo.setPayerTotal(total);
        Gson gson = new Gson();
        String jsonParams = gson.toJson(params);
        paymentInfo.setContent(jsonParams);
        this.save(paymentInfo);
    }
}




