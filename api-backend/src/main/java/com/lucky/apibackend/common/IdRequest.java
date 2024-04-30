package com.lucky.apibackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/4/29
 * @description id请求
 */
@Data
public class IdRequest implements Serializable {
    private static final long serialVersionUID = -5567661176496003385L;

    /**
     * id
     */
    private Long id;
}
