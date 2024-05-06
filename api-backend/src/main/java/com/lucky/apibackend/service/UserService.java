package com.lucky.apibackend.service;

import com.lucky.apibackend.model.dto.user.UserLoginRequest;
import com.lucky.apibackend.model.dto.user.UserRegisterRequest;
import com.lucky.apibackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.apibackend.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author ccc
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-04-16 22:47:51
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    String userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    UserVo getLoginUser(HttpServletRequest request);

    /**
     * 是游客
     * @param request
     * @return
     */
    UserVo isTourist(HttpServletRequest request);

    /**
     * 是否是管理员
     * @param request
     */
    boolean isAdmin(HttpServletRequest request);
}
