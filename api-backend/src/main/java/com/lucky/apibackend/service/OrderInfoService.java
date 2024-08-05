package com.lucky.apibackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.apibackend.model.entity.OrderInfo;
import com.lucky.apibackend.model.enums.OrderStatus;
import com.lucky.apibackend.model.vo.UserVo;

import java.util.List;

/**
* @author ccc
* @description 针对表【order_info(订单信息表)】的数据库操作Service
* @createDate 2024-07-25 01:32:00
*/
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 创建订单
     * @param productId
     * @param paymentType
     * @param loginUser
     * @return
     */
    OrderInfo createOrderInfo(Long productId, String paymentType, UserVo loginUser);

    /**
     * 根据订单号获取订单
     * @param outTradeNo
     * @return
     */
    OrderInfo getOrderByOrderNo(String outTradeNo);

    /**
     * 根据订单号更新订单状态
     * @param outTradeNo
     * @param orderStatus
     */
    void updateStatusByOrderNo(String outTradeNo, OrderStatus orderStatus);

    /**
     * 查询创建超过xx minutes分钟并且未支付的订单
     * @param paymentType
     * @return
     */
    List<OrderInfo> getNoPayOrderByDuration(String paymentType);
}
