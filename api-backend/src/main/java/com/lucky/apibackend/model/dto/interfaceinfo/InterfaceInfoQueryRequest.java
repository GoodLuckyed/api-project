package com.lucky.apibackend.model.dto.interfaceinfo;

import com.lucky.apibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author lucky
 * @date 2024/4/20
 * @description 接口查询请求对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -9041725889711952362L;

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
     * 响应参数
     */
    private List<ResponseParamsField> responseParams;


    /**
     * 返回格式
     */
    private String returnFormat;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;
}
