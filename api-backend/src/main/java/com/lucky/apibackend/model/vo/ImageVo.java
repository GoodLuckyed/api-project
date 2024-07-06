package com.lucky.apibackend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/7/4
 * @description 上传图片状态vo
 */
@Data
public class ImageVo implements Serializable {
    private static final long serialVersionUID = 4996720711390477614L;
    private String uid;
    private String name;
    private String status;
    private String url;
}
