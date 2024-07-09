package com.lucky.apibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/7/8
 * @description 添加用户请求
 */
@Data
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = -3464552698830715851L;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色：user / admin
     */
    private String userRole;

    /**
     * 钱包余额（积分）
     */
    private Integer balance;

}
