package com.lucky.apibackend.common;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/4/17
 * @description 通用返回类
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 835864364839927152L;

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
       this(code,data,"");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage());
    }
}
