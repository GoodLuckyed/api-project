package com.lucky.apibackend.model.dto.order;

import com.lucky.apibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/8/1
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 579120293719976126L;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 订单号
     */
    private String orderNo;


    /**
     * 订单金额（分）
     */
    private Integer total;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 增加积分个数
     */
    private Integer addPoints;

    /**
     * 支付方式
     */
    private String paymentType;

}
