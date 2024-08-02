package com.lucky.apibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.apibackend.common.BaseResponse;
import com.lucky.apibackend.common.DeleteRequest;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.common.ResultUtils;
import com.lucky.apibackend.constant.CommonConstant;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.order.OrderQueryRequest;
import com.lucky.apibackend.model.entity.OrderInfo;
import com.lucky.apibackend.service.AliPayService;
import com.lucky.apibackend.service.OrderInfoService;
import com.lucky.apicommon.model.entity.InterfaceInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lucky
 * @date 2024/8/1
 * @description
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private AliPayService aliPayService;
    @Resource
    private OrderInfoService orderInfoService;

    @ApiOperation(value = "用户取消订单")
    @PostMapping("/trade/close/{orderNo}")
    public BaseResponse<Boolean> cancelOrder(@PathVariable String orderNo){
        aliPayService.closeOrder(orderNo);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取订单列表
     * @param orderQueryRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "分页获取订单列表")
    @PostMapping("/list/page")
    public BaseResponse<Page<OrderInfo>> listOrderByPage(OrderQueryRequest orderQueryRequest, HttpServletRequest request){
        if (orderQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = orderQueryRequest.getTitle();
        String orderNo = orderQueryRequest.getOrderNo();
        Integer total = orderQueryRequest.getTotal();
        String status = orderQueryRequest.getStatus();
        Integer addPoints = orderQueryRequest.getAddPoints();
        String paymentType = orderQueryRequest.getPaymentType();
        long current = orderQueryRequest.getCurrent();
        long pageSize = orderQueryRequest.getPageSize();
        String sortField = orderQueryRequest.getSortField();
        String sortOrder = orderQueryRequest.getSortOrder();
        //限制爬虫
        if (pageSize > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(title),"title",title)
                .eq(StringUtils.isNotBlank(orderNo),"orderNo",orderNo)
                .eq(ObjectUtils.isNotEmpty(total),"total",total)
                .eq(StringUtils.isNotBlank(status),"status",status)
                .eq(ObjectUtils.isNotEmpty(addPoints),"addPoints",addPoints)
                .eq(StringUtils.isNotBlank(paymentType),"payment_type",paymentType)
                .orderBy(StringUtils.isNotBlank(sortField),sortOrder.equals(CommonConstant.SORT_ORDER_ASC),sortField);
        Page<OrderInfo> orderInfoPage = orderInfoService.page(new Page<>(current, pageSize), queryWrapper);
        return ResultUtils.success(orderInfoPage);
    }

    /**
     * 删除订单
     * @param deleteRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "删除订单")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrder(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        if (ObjectUtils.anyNull(deleteRequest,deleteRequest.getId()) || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean remove = orderInfoService.removeById(deleteRequest.getId());
        if (!remove){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据id获取订单信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取订单信息")
    @GetMapping("/get")
    public BaseResponse<OrderInfo> getOrderInfo(long id){
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo orderInfo = orderInfoService.getById(id);
        if (orderInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(orderInfo);
    }
}
