package com.lucky.apibackend.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.apibackend.annontation.AuthCheck;
import com.lucky.apibackend.common.BaseResponse;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.common.IdRequest;
import com.lucky.apibackend.common.ResultUtils;
import com.lucky.apibackend.constant.CommonConstant;
import com.lucky.apibackend.constant.UserConstant;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.lucky.apibackend.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.lucky.apibackend.model.dto.interfaceinfo.RequestParamsField;
import com.lucky.apibackend.model.dto.interfaceinfo.ResponseParamsField;
import com.lucky.apibackend.model.entity.InterfaceInfo;
import com.lucky.apibackend.model.enums.InterfaceStatusEnum;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.InterfaceInfoService;
import com.lucky.apibackend.service.UserService;
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
 * @date 2024/4/18
 * @description 接口信息控制器
 */

@RestController
@RequestMapping("/interface")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 添加接口信息
     * @return
     */
    @ApiOperation(value = "添加接口信息")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (CollectionUtils.isNotEmpty(interfaceInfoAddRequest.getRequestParams())){
            List<RequestParamsField> requestParamsFields = interfaceInfoAddRequest.getRequestParams().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getFieldName()))
                    .collect(Collectors.toList());
            String requestParmas = JSONUtil.toJsonStr(requestParamsFields);
            interfaceInfo.setRequestParams(requestParmas);
        }
        if (CollectionUtils.isNotEmpty(interfaceInfoAddRequest.getResponseParams())){
            List<ResponseParamsField> responseParamsFields = interfaceInfoAddRequest.getResponseParams().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getFieldName()))
                    .collect(Collectors.toList());
            String responseParams = JSONUtil.toJsonStr(responseParamsFields);
            interfaceInfo.setResponseParams(responseParams);
        }
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        //校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo,true);
        //获取当前用户
        UserVo loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean save = interfaceInfoService.save(interfaceInfo);
        if (save){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(interfaceInfo.getId());
    }

    /**
     * 根据id获取接口信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取接口信息")
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfo(long id){
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取接口列表（仅限管理员使用）
     * @return
     */
    @ApiOperation(value = "获取接口列表")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest){
        if (interfaceInfoQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest,interfaceInfo);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取接口列表
     * @param interfaceInfoQueryRequest
     * @return
     */
    @ApiOperation(value = "分页获取接口列表")
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest,HttpServletRequest request){
        if (interfaceInfoQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest,interfaceInfoQuery);
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String returnFormat = interfaceInfoQueryRequest.getReturnFormat();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        long current = interfaceInfoQueryRequest.getCurrent();
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        //限制爬虫
        if (pageSize > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),"name",name)
                .like(StringUtils.isNotBlank(description),"description",description)
                .like(StringUtils.isNotBlank(url),"url",url)
                .like(StringUtils.isNotBlank(returnFormat),"return_format",returnFormat)
                .eq(ObjectUtils.isNotEmpty(status),"status",status)
                .eq(StringUtils.isNotBlank(method),"method",method)
                .orderBy(StringUtils.isNotEmpty(sortField),sortOrder.equals(CommonConstant.SORT_ORDER_ASC),sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, pageSize), queryWrapper);
        UserVo user = userService.isTourist(request);

        //不是管理员只能查看已上线的接口
        if (user == null || !user.getUserRole().equals(UserConstant.ADMIN_ROLE)){
            List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords().stream().
                    filter(interfaceInfo -> interfaceInfo.getStatus().equals(InterfaceStatusEnum.OFFLINE.getValue()))
                    .collect(Collectors.toList());
            interfaceInfoPage.setRecords(interfaceInfoList);
        }
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 发布接口
     * @param idRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "发布接口")
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request){
        if (ObjectUtils.anyNull(idRequest,idRequest.getId()) || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //将接口状态设置为上线
        interfaceInfo.setStatus(InterfaceStatusEnum.ONLINE.getValue());
        return ResultUtils.success(interfaceInfoService.updateById(interfaceInfo));
    }

    @ApiOperation(value = "下线接口")
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request){
        if (ObjectUtils.anyNull(idRequest,idRequest.getId()) || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //将接口状态设置为下线
        interfaceInfo.setStatus(InterfaceStatusEnum.OFFLINE.getValue());
        return ResultUtils.success(interfaceInfoService.updateById(interfaceInfo));
    }
}































