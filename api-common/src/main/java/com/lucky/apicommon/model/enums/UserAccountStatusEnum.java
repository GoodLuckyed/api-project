package com.lucky.apicommon.model.enums;

/**
 * @author lucky
 * @date 2024/4/17
 * @description 用户账号状态枚举
 */
public enum UserAccountStatusEnum {
    NORMAL(0, "正常"),
    BAN(1, "封禁");

    private final Integer value;

    private final String text;

    UserAccountStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
