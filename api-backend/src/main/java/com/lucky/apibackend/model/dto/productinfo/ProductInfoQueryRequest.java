package com.lucky.apibackend.model.dto.productinfo;

import com.lucky.apibackend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lucky
 * @date 2024/7/11
 * @description 查询产品请求
 */
@Data
public class ProductInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -6506580230803973058L;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 金额（单位：分）
     */
    private Integer total;

    /**
     * 增加积分个数
     */
    private Integer addPoints;

    /**
     * 产品类型（VIP-会员  COIN-充值  COINACTIVITY-充值活动）
     */
    private String productType;
}
