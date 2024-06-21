package com.lucky.apigateway.exception;

import com.lucky.apicommon.common.ErrorCode;

/**
 * @author lucky
 * @date 2024/4/17
 * @description 自定义异常类
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 6253093432660571027L;
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message){
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
