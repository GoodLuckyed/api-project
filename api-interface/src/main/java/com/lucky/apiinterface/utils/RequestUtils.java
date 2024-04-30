package com.lucky.apiinterface.utils;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

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
}
