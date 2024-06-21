package com.lucky.apibackend.service.inner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lucky.apibackend.service.InterfaceInfoService;
import com.lucky.apicommon.model.entity.InterfaceInfo;
import com.lucky.apicommon.service.inner.InnerInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author lucky
 * @date 2024/6/19
 * @description
 */
@DubboService
@Slf4j
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 获取接口信息
     * @param path
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        // 如果带参数，去除第一个？和之后后的参数
        if (path.contains("?")){
            path = path.substring(0,path.indexOf("?"));
        }
        if (path.startsWith("http://")){
            path = path.substring(7);
        }
        if (path.startsWith("https://")){
            path = path.substring(8);
        }
        log.info("【查询地址base url】" + path);
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfo::getMethod,method);
        queryWrapper.like(InterfaceInfo::getUrl,path);
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(queryWrapper);
        return interfaceInfo;
    }
}
