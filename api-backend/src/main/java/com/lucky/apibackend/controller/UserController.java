package com.lucky.apibackend.controller;

import com.lucky.apibackend.common.BaseResponse;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.common.ResultUtils;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.user.UserLoginRequest;
import com.lucky.apibackend.model.dto.user.UserRegisterRequest;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lucky
 * @date 2024/4/16
 * @description 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result =  userService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String result =  userService.userLogin(userLoginRequest,request);
        return ResultUtils.success(result);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @ApiOperation(value = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result =  userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @ApiOperation("获取当前登录用户")
    @GetMapping("/get/login")
    public BaseResponse<UserVo> getLoginUser(HttpServletRequest request){
        UserVo user = userService.getLoginUser(request);
        return ResultUtils.success(user);
    }
}

























