package com.lucky.apibackend.model.enums;

import lombok.Getter;

/**
 * @author lucky
 * @date 2024/7/26
 * @description
 */
@Getter
public enum ProductTypeEnum {
    /**
     * vip
     */
    VIP("VIP"),
    /**
     * 充值
     */
    COIN("COIN"),
    /**
     * 充值活动
     */
    COINACTIVITY("COINACTIVITY");

    private final String value;

    ProductTypeEnum(String value) {
        this.value = value;
    }
}
