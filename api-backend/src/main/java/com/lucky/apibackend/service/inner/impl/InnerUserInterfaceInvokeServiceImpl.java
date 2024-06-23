package com.lucky.apibackend.service.inner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.mapper.UserInterfaceInvokeMapper;
import com.lucky.apibackend.model.entity.UserInterfaceInvoke;
import com.lucky.apibackend.service.InterfaceInfoService;
import com.lucky.apibackend.service.UserService;
import com.lucky.apicommon.service.inner.InnerUserInterfaceInvokeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author lucky
 * @date 2024/6/22
 * @description
 */
@DubboService
public class InnerUserInterfaceInvokeServiceImpl extends ServiceImpl<UserInterfaceInvokeMapper,UserInterfaceInvoke>
        implements InnerUserInterfaceInvokeService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 接口调用
     * @param interfaceInfoId 接口id
     * @param userId 调用者id
     * @param reduceScore 消耗积分数
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean invoke(Long interfaceInfoId, Long userId, Integer reduceScore) {
        LambdaQueryWrapper<UserInterfaceInvoke> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInvoke::getInterfaceId, interfaceInfoId);
        queryWrapper.eq(UserInterfaceInvoke::getUserId, userId);
        UserInterfaceInvoke userInterfaceInvoke = this.getOne(queryWrapper);
        boolean invokeResult;
        // 判断该记录是否存在 不存在就创建一条记录
        if (userInterfaceInvoke == null) {
           userInterfaceInvoke = new UserInterfaceInvoke();
           userInterfaceInvoke.setInterfaceId(interfaceInfoId);
           userInterfaceInvoke.setUserId(userId);
           userInterfaceInvoke.setTotalInvokes(1L);
           invokeResult = this.save(userInterfaceInvoke);
        }else {
            // 存在就更新该用户对接口的调用次数
            userInterfaceInvoke.setTotalInvokes(userInterfaceInvoke.getTotalInvokes() + 1);
            invokeResult = this.updateById(userInterfaceInvoke);
        }
        // 更新接口的总调用次数
        boolean interfaceUpdateResult = interfaceInfoService.updateTotalInvokes(interfaceInfoId);
        // 更新用户钱包积分
        boolean reduceWalletBalanceResult = userService.reduceWalletBalance(userId, reduceScore);
        boolean updateResult = invokeResult && interfaceUpdateResult && reduceWalletBalanceResult;
        if (!updateResult){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用失败");
        }
        return true;
    }
}
