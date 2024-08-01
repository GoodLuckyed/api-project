package com.lucky.apibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.lucky.apibackend.annontation.AuthCheck;
import com.lucky.apibackend.common.*;
import com.lucky.apibackend.constant.CommonConstant;
import com.lucky.apibackend.constant.UserConstant;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.user.*;
import com.lucky.apibackend.model.entity.User;
import com.lucky.apibackend.model.enums.UserAccountStatusEnum;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.UserService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 获取用户列表
     * @param userQueryRequest
     * @param request
     * @return
     */
    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public BaseResponse<List<UserVo>> listUser(UserQueryRequest userQueryRequest,HttpServletRequest request){
        if (userQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User userQuery = new User();
        BeanUtils.copyProperties(userQueryRequest,userQuery);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>(userQuery);
        List<User> userList = userService.list(queryWrapper);
        List<UserVo> userVoList = null;
        if (CollectionUtils.isNotEmpty(userList)){
            userVoList = userList.stream().map(user -> {
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(user, userVo);
                return userVo;
            }).collect(Collectors.toList());
        }
        return ResultUtils.success(userVoList);
    }

    /**
     * 分页获取用户列表
     * @param userQueryRequest
     * @param request
     * @return
     */
    @ApiOperation("分页获取用户列表")
    @GetMapping("/list/page")
    public BaseResponse<Page<UserVo>> listUserByPage(UserQueryRequest userQueryRequest,HttpServletRequest request){
        if (userQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User userQuery = new User();
        BeanUtils.copyProperties(userQueryRequest,userQuery);

        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        Integer gender = userQueryRequest.getGender();
        String userRole = userQueryRequest.getUserRole();
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(userName),"userName",userName)
                .eq(StringUtils.isNotBlank(userAccount),"userAccount",userAccount)
                .eq(gender != null,"gender",gender)
                .eq(StringUtils.isNotBlank(userRole),"userRole",userRole)
                .orderBy(StringUtils.isNotBlank(sortField),sortOrder.equals(CommonConstant.SORT_ORDER_ASC),sortField);
        Page<User> userPage = userService.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserVo> userVoPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVo> userVoList = userPage.getRecords().stream().map(user -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());
        userVoPage.setRecords(userVoList);
        return ResultUtils.success(userVoPage);
    }

    /**
     * 添加用户
     * @param userAddRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "添加用户")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request){
        if (userAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest,user);
        userService.validUser(user,true);
        boolean save = userService.save(user);
        if (!save){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 修改用户
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "修改用户")
    @PostMapping("/update")
    public BaseResponse<UserVo> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request){
        if (ObjectUtils.allNull(userUpdateRequest,userUpdateRequest.getId()) || userUpdateRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 管理员才能处理的业务
        boolean adminOperation = ObjectUtils.isNotEmpty(userUpdateRequest.getBalance())
                || StringUtils.isNotBlank(userUpdateRequest.getUserRole())
                || StringUtils.isNotBlank(userUpdateRequest.getUserPassword());
        UserVo loginUser = userService.getLoginUser(request);
        if (adminOperation && !loginUser.getUserRole().equals(UserConstant.ADMIN_ROLE)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 账户只有本人和管理员可以修改
        if (!loginUser.getId().equals(userUpdateRequest.getId()) && !loginUser.getUserRole().equals(UserConstant.ADMIN_ROLE)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"只有本人或管理员可以修改");
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest,user);
        userService.validUser(user,false);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userUpdateRequest.getId());
        boolean update = userService.update(user, queryWrapper);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"修改失败");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userService.getById(user.getId()),userVo);
        return ResultUtils.success(userVo);
    }

    /**
     * 删除用户
     * @param deleteRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        if (ObjectUtils.anyNull(deleteRequest,deleteRequest.getId()) || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean del = userService.removeById(deleteRequest.getId());
        if (!del){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 封禁用户
     * @param idRequest
     * @return
     */
    @ApiOperation(value = "封禁用户")
    @PostMapping("/ban")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> banUser(@RequestBody IdRequest idRequest){
        if (ObjectUtils.anyNull(idRequest,idRequest.getId()) || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(idRequest.getId());
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        user.setStatus(UserAccountStatusEnum.BAN.getValue());
        boolean update = userService.updateById(user);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 解封
     * @param idRequest
     * @return
     */
    @ApiOperation(value = "解封")
    @PostMapping("/normal")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> normalUser(@RequestBody IdRequest idRequest) {
        if (ObjectUtils.anyNull(idRequest,idRequest.getId()) || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(idRequest.getId());
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        user.setStatus(UserAccountStatusEnum.NORMAL.getValue());
        boolean update = userService.updateById(user);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 实时获取用户积分
     * @param request
     * @return
     */
    @GetMapping("/getBalance")
    public BaseResponse<Integer> getBalance(HttpServletRequest request){
        UserVo loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        User user = userService.getById(userId);
        Integer balance = user.getBalance();
        return ResultUtils.success(balance);
    }
}

























