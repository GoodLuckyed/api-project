package com.lucky.apibackend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/7/6
 * @description
 */
@Data
public class InterfaceUploadAvatarRequest implements Serializable {
    private static final long serialVersionUID = 2696762337545010735L;

    /**
     * 接口id
     */
    private long id;

    /**
     * 接口头像url
     */
    private String avatarUrl;
}
