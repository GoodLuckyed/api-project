package com.lucky.apibackend.model.enums;

/**
 * @author lucky
 * @date 2024/4/20
 * @description 产品状态枚举
 */
public enum ProductStatusEnum {
    OFFLINE("关闭",0),
    ONLINE("开启",1),
    AUDITING("审核中",2);

    private final String text;

    private final Integer value;

    ProductStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public Integer getValue() {
        return value;
    }
}
