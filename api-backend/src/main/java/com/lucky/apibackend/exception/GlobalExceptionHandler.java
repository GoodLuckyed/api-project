package com.lucky.apibackend.exception;

import com.lucky.apibackend.common.BaseResponse;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lucky
 * @date 2024/4/17
 * @description 全局异常处理
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?>businessExceptionHandler(BusinessException e){
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage());
    }
}
