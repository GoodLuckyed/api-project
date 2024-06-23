package com.lucky.apicommon.service.inner;

/**
 * @author lucky
 * @date 2024/6/22
 * @description 接口调用服务
 */
public interface InnerUserInterfaceInvokeService {

    /**
     * 接口调用
     * @param interfaceInfoId 接口id
     * @param userId 调用者id
     * @param reduceScore 消耗积分数
     * @return boolean
     */
    boolean invoke(Long interfaceInfoId, Long userId, Integer reduceScore);
}
