package com.lucky.apibackend.task;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lucky.apibackend.model.entity.OrderInfo;
import com.lucky.apibackend.model.enums.PayType;
import com.lucky.apibackend.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lucky
 * @date 2024/8/3
 * @description
 */
@Slf4j
@Component
public class AliPayTask {

    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 从第0秒开始每隔30秒执行1次，查询创建超过10分钟，并且未支付的订单
     */
//    @Scheduled(cron = "0/30 * * * * ?")
    public void orderConfirm() {

        List<OrderInfo> orderInfoList = orderInfoService.getNoPayOrderByDuration(PayType.ALIPAY.getType());
        if (CollectionUtils.isNotEmpty(orderInfoList)){
            for (OrderInfo orderInfo : orderInfoList) {
                // todo 调用支付宝查单接口
                // todo 更新本地相关数据
            }
        }
    }
}
