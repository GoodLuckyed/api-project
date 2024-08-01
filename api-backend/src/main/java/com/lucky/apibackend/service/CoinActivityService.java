package com.lucky.apibackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.apibackend.model.entity.CoinActivity;
import com.lucky.apibackend.model.entity.OrderInfo;

/**
* @author ccc
* @description 针对表【coin_activity(充值活动表)】的数据库操作Service
* @createDate 2024-07-26 00:19:25
*/
public interface CoinActivityService extends IService<CoinActivity> {

    /**
     * 判断购买的是否是充值活动的产品
     * @param userId
     * @param productId
     */
    void checkBuyCoinActivity(Long userId, Long productId);

    /**
     * 判断购买的是否是充值活动的产品
     * @param orderInfo
     */
    void saveRechargeActivity(OrderInfo orderInfo);
}
