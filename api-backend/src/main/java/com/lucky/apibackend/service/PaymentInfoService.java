package com.lucky.apibackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.apibackend.model.entity.PaymentInfo;

import java.util.Map;

/**
* @author ccc
* @description 针对表【payment_info(支付信息表)】的数据库操作Service
* @createDate 2024-07-25 01:32:30
*/
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     * 记录支付信息
     * @param params
     */
    void createPaymentInfo(Map<String, String> params);
}
