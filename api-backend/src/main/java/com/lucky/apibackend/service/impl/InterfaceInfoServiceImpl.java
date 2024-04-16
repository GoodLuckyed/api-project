package com.lucky.apibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.apibackend.model.entity.InterfaceInfo;
import com.lucky.apibackend.service.InterfaceInfoService;
import com.lucky.apibackend.mapper.InterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author ccc
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2024-04-16 22:53:10
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

}




