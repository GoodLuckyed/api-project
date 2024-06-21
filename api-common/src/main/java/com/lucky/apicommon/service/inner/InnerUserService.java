package com.lucky.apicommon.service.inner;

import com.lucky.apicommon.model.vo.UserVo;

/**
 * @author lucky
 * @date 2024/6/18
 * @description 获取invoke用户服务
 */
public interface InnerUserService {

    /**
     * 通过访问密钥获取invoke用户
     * @param accessKey
     * @return
     */
    UserVo getInvokeUserByAccessKey(String accessKey);
}
