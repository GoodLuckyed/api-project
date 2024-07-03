package com.lucky.apibackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/6/28
 * @description
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 53792169977834800L;

    private Long id;
}
