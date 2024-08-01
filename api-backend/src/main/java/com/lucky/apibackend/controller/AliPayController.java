package com.lucky.apibackend.controller;

import com.lucky.apibackend.common.BaseResponse;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.common.ResultUtils;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.alipay.PayCreateRequest;
import com.lucky.apibackend.service.AliPayService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lucky
 * @date 2024/7/25
 * @description 支付宝接口控制器
 */
@RestController
@RequestMapping("/aliPay")
@Slf4j
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    /**
     * 手机网站支付接口
     * @param payCreateRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "手机网站支付接口")
    @PostMapping("/trade/pay")
    public BaseResponse<String> tradePay(@RequestBody(required = false) PayCreateRequest payCreateRequest, HttpServletRequest request) {
        if (payCreateRequest == null || payCreateRequest.getProductId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String formStr = aliPayService.tradePay(payCreateRequest,request);
        return ResultUtils.success(formStr);
    }

    @ApiOperation("支付通知")
    @PostMapping("/trade/notify")
    public String tradeNotify(@RequestParam Map<String, String> params) {
        log.info("支付通知参数：{}",params);
        return aliPayService.tradeNotify(params);
    }
}
















