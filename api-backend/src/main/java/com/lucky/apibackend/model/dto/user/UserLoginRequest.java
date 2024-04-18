package com.lucky.apibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/4/17
 * @description 用户登录请求对象
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 3032691790934324053L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
