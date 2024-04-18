package com.lucky.apibackend.model.dto.user;


import lombok.Data;

import java.io.Serializable;


/**
 * @author lucky
 * @date 2024/4/17
 * @description 用户注册请求对象
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -1833754656471566110L;

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
     * 确认密码
     */
    private String checkPassword;

}
