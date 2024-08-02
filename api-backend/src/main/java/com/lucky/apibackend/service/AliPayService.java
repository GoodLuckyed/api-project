package com.lucky.apibackend.service;

import com.lucky.apibackend.model.dto.alipay.PayCreateRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lucky
 * @date 2024/7/25
 * @description
 */
public interface AliPayService {

    /**
     * 手机网站支付
     * @param payCreateRequest
     * @param request
     * @return
     */
    String tradePay(PayCreateRequest payCreateRequest, HttpServletRequest request);

    /**
     * 支付通知
     * @param params
     * @return
     */
    String tradeNotify(Map<String, String> params);

    /**
     * 用户取消订单
     * @param orderNo
     */
    void closeOrder(String orderNo);
}
