package com.lucky.apibackend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.mapper.CoinActivityMapper;
import com.lucky.apibackend.model.entity.CoinActivity;
import com.lucky.apibackend.model.entity.OrderInfo;
import com.lucky.apibackend.model.entity.ProductInfo;
import com.lucky.apibackend.model.enums.ProductTypeEnum;
import com.lucky.apibackend.service.CoinActivityService;
import com.lucky.apibackend.service.ProductInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author ccc
* @description 针对表【coin_activity(充值活动表)】的数据库操作Service实现
* @createDate 2024-07-26 00:19:25
*/
@Service
public class CoinActivityServiceImpl extends ServiceImpl<CoinActivityMapper, CoinActivity> implements CoinActivityService {

    @Resource
    private ProductInfoService productInfoService;

    /**
     * 判断购买的是否是充值活动的产品
     * @param userId
     * @param productId
     */
    @Override
    public void checkBuyCoinActivity(Long userId, Long productId) {
        LambdaQueryWrapper<CoinActivity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoinActivity::getUserId,userId);
        queryWrapper.eq(CoinActivity::getProductId,productId);
        long count = this.count(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"该产品只能购买一次，请在订单列表查看是否已经创建了该订单");
        }
    }

    @Override
    public void saveRechargeActivity(OrderInfo orderInfo) {
        Long productId = orderInfo.getProductId();
        ProductInfo productInfo = productInfoService.getById(productId);
        if (productInfo.getProductType().equals(ProductTypeEnum.COINACTIVITY.getValue())){
            CoinActivity coinActivity = new CoinActivity();
            coinActivity.setUserId(orderInfo.getUserId());
            coinActivity.setProductId(productId);
            coinActivity.setOrderNo(orderInfo.getOrderNo());
            this.save(coinActivity);
        }
    }
}




