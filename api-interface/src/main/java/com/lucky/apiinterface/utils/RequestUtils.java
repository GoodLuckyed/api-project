package com.lucky.apiinterface.utils;

import cn.hutool.http.HttpRequest;
import com.lucky.apisdk.exception.ApiException;
import com.lucky.apisdk.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author lucky
 * @date 2024/4/29
 * @description 请求工具类
 */
@Slf4j
public class RequestUtils {

    /**
     * 发送get请求
     * @param url
     * @return
     */
    public static String get(String url){
        String body = HttpRequest.get(url).execute().body();
        log.info("【interface】：请求url：{}，返回结果：{}",url,body);
        return body;
    }

    /**
     * 构建url
     * @param baseUrl
     * @param params
     * @return
     * @param <T>
     */
    public static <T> String buildUrl(String baseUrl,T params) throws ApiException {
        StringBuilder builder = new StringBuilder(baseUrl);
        // 获取所有属性
        Field[] fields = params.getClass().getDeclaredFields();
        boolean isFirstParam = true;
        for (Field field : fields) {
            // 设置私有属性可访问
            field.setAccessible(true);
            String name = field.getName();
            // 跳过serialVersionUID属性
            if ("serialVersionUID".equals(name)){
                continue;
            }
            // 获取属性的值
            try {
                Object value = field.get(params);
                if (value != null){
                    if (isFirstParam){
                        builder.append("?").append(name).append("=").append(value);
                        isFirstParam = false;
                    }else {
                        builder.append("&").append(name).append("=").append(value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new ApiException(ErrorCode.OPERATION_ERROR,"构建url异常");
            }
        }
        return builder.toString();
    }
}
