package com.lucky.apibackend.model.dto.interfaceinfo;

import lombok.Data;

/**
 * @author lucky
 * @date 2024/4/18
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
