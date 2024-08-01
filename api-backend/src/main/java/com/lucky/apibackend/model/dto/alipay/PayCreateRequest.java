package com.lucky.apibackend.model.dto.alipay;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/7/25
 * @description
 */
@Data
public class PayCreateRequest implements Serializable {

    private static final long serialVersionUID = 9162524641285848847L;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 支付方式
     */
    private String paymentType;

}
