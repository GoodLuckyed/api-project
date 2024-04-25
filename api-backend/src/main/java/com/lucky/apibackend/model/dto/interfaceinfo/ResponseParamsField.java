package com.lucky.apibackend.model.dto.interfaceinfo;

import lombok.Data;

/**
 * @author lucky
 * @date 2024/4/18
 * @description 响应参数字段
 */
@Data
public class ResponseParamsField {
    private String id;
    private String fieldName;
    private String type;
    private String desc;
}
