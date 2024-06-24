package com.lucky.apibackend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lucky
 * @date 2024/4/18
 * @description 添加接口信息请求对象
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {
    private static final long serialVersionUID = -2717300219710645905L;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private List<RequestParamsField> requestParams;

    /**
     * 响应参数
     */
    private List<ResponseParamsField> responseParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 返回格式
     */
    private String returnFormat;

    /**
     * 请求示例
     */
    private String requestExample;

    /**
     * 消耗积分数
     */
    private Integer reduceScore;

    /**
     * 请求类型
     */
    private String method;
}
