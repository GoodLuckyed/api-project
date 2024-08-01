package com.lucky.apibackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单信息表
 * @TableName order_info
 */
@TableName(value ="order_info")
@Data
public class OrderInfo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 二维码地址
     */
    private String codeUrl;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 支付产品id
     */
    private Long productId;

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

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}