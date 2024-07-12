package com.lucky.apibackend.model.dto.productinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lucky
 * @date 2024/7/11
 * @description 添加产品请求
 */
@Data
public class ProductInfoAddRequest implements Serializable {
    private static final long serialVersionUID = 1395145116777038584L;

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

    /**
     * 过期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirationTime;
}
