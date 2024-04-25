package com.lucky.apibackend.common;

import com.lucky.apibackend.constant.CommonConstant;
import lombok.Data;

/**
 * @author lucky
 * @date 2024/4/20
 * @description 分页请求
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}