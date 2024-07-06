package com.lucky.apibackend.model.enums;

/**
 * @author lucky
 * @date 2024/7/6
 * @description 图片状态枚举
 */
public enum ImageStatusEnum {
    SUCCESS("success", "done"),
    ERROR("error", "error");

    private String status;
    private String value;

    ImageStatusEnum(String status, String value) {
        this.status = status;
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public String getValue() {
        return value;
    }
}
