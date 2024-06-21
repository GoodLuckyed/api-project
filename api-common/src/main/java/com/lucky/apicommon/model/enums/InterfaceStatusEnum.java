package com.lucky.apicommon.model.enums;

/**
 * @author lucky
 * @date 2024/4/20
 * @description 接口状态枚举
 */
public enum InterfaceStatusEnum {
    OFFLINE("关闭",0),
    ONLINE("开启",1),
    AUDITING("审核中",2);

    private final String text;

    private final Integer value;

    InterfaceStatusEnum(String text, Integer value) {
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
