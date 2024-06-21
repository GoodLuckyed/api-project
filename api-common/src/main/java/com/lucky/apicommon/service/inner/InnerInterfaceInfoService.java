package com.lucky.apicommon.service.inner;

import com.lucky.apicommon.model.entity.InterfaceInfo;

/**
 * @author lucky
 * @date 2024/6/19
 * @description 接口信息服务
 */
public interface InnerInterfaceInfoService {

    /**
     * 获取接口信息
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
