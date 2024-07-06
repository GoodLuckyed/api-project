package com.lucky.apibackend.model.enums;

import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author lucky
 * @date 2024/7/6
 * @description 文件上传业务类型枚举
 */
public enum FileUploadBizEnum {
    USER_AVATAR("用户头像", "user_avatar"),
    INTERFACE_AVATAR("接口图片", "interface_avatar");

    private String text;
    private String value;

    FileUploadBizEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static FileUploadBizEnum getEnumByValue(String value){
        if (ObjectUtils.isEmpty(value)){
            return null;
        }
        for (FileUploadBizEnum fileUploadBizEnum : FileUploadBizEnum.values()) {
            if (fileUploadBizEnum.value.equals(value)){
                return fileUploadBizEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
