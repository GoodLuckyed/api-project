package com.lucky.apibackend.model.enums;

import lombok.Getter;

/**
 * @author lucky
 * @date 2024/7/29
 * @description
 */
@Getter
public enum PayType {
    /**
     * 微信
     */
    WXPAY("微信"),


    /**
     * 支付宝
     */
    ALIPAY("支付宝");

    /**
     * 类型
     */
    private final String type;

    PayType(String type) {
        this.type = type;
    }
}
