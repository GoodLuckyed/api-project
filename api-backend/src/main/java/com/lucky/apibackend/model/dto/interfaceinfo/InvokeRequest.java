package com.lucky.apibackend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lucky
 * @date 2024/5/23
 * @description 调用请求
 */
@Data
public class InvokeRequest implements Serializable {
    private static final long serialVersionUID = -557244978779324149L;

    private Long id;
    private List<Field> requestParams;
    private String userRequestParams;

    @Data
    public static class Field{
        private String fieldName;
        private String value;
    }
}
