package com.lucky.apibackend.model.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author lucky
 * @date 2024/7/25
 * @description
 */
@Getter
public enum OrderStatus {

    /**
     * 未支付
     */
    NOTPAY("未支付"),


    /**
     * 支付成功
     */
    SUCCESS("支付成功"),

    /**
     * 已关闭
     */
    CLOSED("超时已关闭"),

    /**
     * 已取消
     */
    CANCEL("用户已取消"),

    /**
     * 退款中
     */
    REFUND_PROCESSING("退款中"),

    /**
     * 已退款
     */
    REFUND_SUCCESS("已退款"),

    /**
     * 退款异常
     */
    REFUND_ABNORMAL("退款异常");

    /**
     * 类型
     */
    private final String type;


    OrderStatus(String type) {
        this.type = type;
    }
}
