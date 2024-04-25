package com.lucky.apibackend.constant;

/**
 * @author lucky
 * @date 2024/4/17
 * @description 用户相关常量
 */
public class UserConstant {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "lucky";

    /**
     * 用户登录状态
     */
    public static final String USER_LOGIN_STATE = "userLoginState";

    /**
     * 默认权限
     */
    public static final String DEFAULT_ROLE = "user";

    /**
     * 管理员权限
     */
    public static final String ADMIN_ROLE = "admin";
}
