package com.lucky.apibackend.service;

import com.lucky.apicommon.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ccc
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-04-16 22:53:10
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验接口信息
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 更新接口总调用次数
     * @param interfaceInfoId 接口id
     * @return
     */
    boolean updateTotalInvokes(Long interfaceInfoId);
}
