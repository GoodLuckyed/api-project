package com.lucky.apibackend.model.file;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/7/4
 * @description
 */
@Data
public class UploadFileRequest implements Serializable {
    private static final long serialVersionUID = -959786060296943352L;

    /**
     * 业务类型（接口照片、用户头像、、、）
     */
    private String biz;
}
