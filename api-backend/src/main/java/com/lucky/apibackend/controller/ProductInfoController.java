package com.lucky.apibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.apibackend.annontation.AuthCheck;
import com.lucky.apibackend.common.*;
import com.lucky.apibackend.constant.UserConstant;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.productinfo.ProductInfoAddRequest;
import com.lucky.apibackend.model.dto.productinfo.ProductInfoQueryRequest;
import com.lucky.apibackend.model.dto.productinfo.ProductInfoUpdateRequest;
import com.lucky.apibackend.model.entity.ProductInfo;
import com.lucky.apibackend.model.enums.InterfaceStatusEnum;
import com.lucky.apibackend.model.enums.ProductStatusEnum;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.ProductInfoService;
import com.lucky.apibackend.service.UserService;
import com.lucky.apicommon.model.entity.InterfaceInfo;
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
 * @date 2024/7/11
 * @description 产品信息控制器
 */
@RestController
@RequestMapping("/productInfo")
public class ProductInfoController {

    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private UserService userService;

    /**
     * 添加产品
     *
     * @param productInfoAddRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "添加产品")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addProductInfo(@RequestBody ProductInfoAddRequest productInfoAddRequest, HttpServletRequest request) {
        if (productInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoAddRequest, productInfo);
        // 校验参数
        productInfoService.validProductInfo(productInfo, true);
        UserVo loginUser = userService.getLoginUser(request);
        productInfo.setUserId(loginUser.getId());
        boolean save = productInfoService.save(productInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(productInfo.getId());
    }

    /**
     * 修改产品
     *
     * @param productInfoUpdateRequest
     * @return
     */
    @ApiOperation(value = "修改产品")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateProductInfo(@RequestBody ProductInfoUpdateRequest productInfoUpdateRequest) {
        if (ObjectUtils.anyNull(productInfoUpdateRequest, productInfoUpdateRequest.getId()) || productInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断产品是否存在
        long id = productInfoUpdateRequest.getId();
        ProductInfo oldProductInfo = productInfoService.getById(id);
        if (oldProductInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoUpdateRequest, productInfo);
        // 校验参数
        productInfoService.validProductInfo(productInfo, false);
        boolean update = productInfoService.updateById(productInfo);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 删除产品
     *
     * @param deleteRequest
     * @return
     */
    @ApiOperation(value = "删除产品")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> delProductInfo(@RequestBody DeleteRequest deleteRequest) {
        if (ObjectUtils.anyNull(deleteRequest, deleteRequest.getId()) || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品不存在");
        }
        boolean del = productInfoService.removeById(id);
        if (!del) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据id获取产品信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取产品信息")
    @GetMapping("/get")
    public BaseResponse<ProductInfo> getProductInfo(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(productInfo);
    }

    /**
     * 获取产品列表（仅限管理员使用）
     *
     * @return
     */
    @ApiOperation(value = "获取产品列表")
    @GetMapping("/list")
    public BaseResponse<List<ProductInfo>> listInterfaceInfo(ProductInfoQueryRequest productInfoQueryRequest) {
        if (productInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoQueryRequest, productInfo);
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>(productInfo);
        List<ProductInfo> productInfoList = productInfoService.list(queryWrapper);
        return ResultUtils.success(productInfoList);
    }

    /**
     * 分页获取产品列表
     *
     * @param productInfoQueryRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "分页获取产品列表")
    @GetMapping("/list/page")
    public BaseResponse<Page<ProductInfo>> listProductInfoByPage(ProductInfoQueryRequest productInfoQueryRequest, HttpServletRequest request) {
        if (productInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfoQuery = new ProductInfo();
        BeanUtils.copyProperties(productInfoQueryRequest, productInfoQuery);
        String name = productInfoQueryRequest.getName();
        String description = productInfoQueryRequest.getDescription();
        Integer total = productInfoQueryRequest.getTotal();
        Integer addPoints = productInfoQueryRequest.getAddPoints();
        String productType = productInfoQueryRequest.getProductType();
        long current = productInfoQueryRequest.getCurrent();
        long pageSize = productInfoQueryRequest.getPageSize();
        String sortField = productInfoQueryRequest.getSortField();
        String sortOrder = productInfoQueryRequest.getSortOrder();

        //限制爬虫
        if (pageSize > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(description), "description", description)
                .eq(StringUtils.isNotBlank(productType), "productType", productType)
                .eq(ObjectUtils.isNotEmpty(total), "total", total)
                .eq(ObjectUtils.isNotEmpty(addPoints), "addPoints", addPoints)
                .orderByAsc("total");
        Page<ProductInfo> productInfoPage = productInfoService.page(new Page<>(current, pageSize), queryWrapper);
        //不是管理员只能查看已上线的接口
        if (!userService.isAdmin(request)) {
            List<ProductInfo> productInfoList = productInfoPage.getRecords().stream().filter(productInfo ->
                    productInfo.getStatus().equals(ProductStatusEnum.ONLINE.getValue())).collect(Collectors.toList());
            productInfoPage.setRecords(productInfoList);
        }
        return ResultUtils.success(productInfoPage);
    }

    /**
     * 发布产品
     * @param idRequest
     * @return
     */
    @ApiOperation(value = "发布产品")
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineProductInfo(@RequestBody IdRequest idRequest){
        if (ObjectUtils.anyNull(idRequest,idRequest.getId()) || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //将产品状态设置为上线
        productInfo.setStatus(ProductStatusEnum.ONLINE.getValue());
        return ResultUtils.success(productInfoService.updateById(productInfo));
    }

    /**
     * 下线产品
     * @param idRequest
     * @return
     */
    @ApiOperation(value = "下线产品")
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineProductInfo(@RequestBody IdRequest idRequest){
        if (ObjectUtils.anyNull(idRequest,idRequest.getId()) || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //将产品状态设置为下线
        productInfo.setStatus(ProductStatusEnum.OFFLINE.getValue());
        return ResultUtils.success(productInfoService.updateById(productInfo));
    }
}






















