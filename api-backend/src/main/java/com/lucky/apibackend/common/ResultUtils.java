package com.lucky.apibackend.common;

/**
 * @author lucky
 * @date 2024/4/17
 * @description 返回工具类
 */
public class ResultUtils {


    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T>  success(T data){
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 错误
     * @param code
     * @param message
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(int code,String message){
        return new BaseResponse<>(code,null,message);
    }

    /**
     * 错误
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 错误
     * @param data
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(T data,ErrorCode errorCode){
        return new BaseResponse<>(errorCode.getCode(),data,errorCode.getMessage());
    }

    /**
     * 错误
     * @param data
     * @param errorCode
     * @param message
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(T data,ErrorCode errorCode,String message){
        return new BaseResponse<>(errorCode.getCode(),data,message);
    }

    /**
     * 错误
     * @param errorCode
     * @param message
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String message){
        return new BaseResponse<>(errorCode.getCode(),null,message);
    }
}
