package com.lucky.apicommon.model.dto;

import lombok.Data;

/**
 * @author lucky
 * @date 2024/6/22
 * @description 请求参数字段
 */
@Data
public class RequestParamsField {
    private String id;
    private String fieldName;
    private String type;
    private String desc;
    private String required;
}
