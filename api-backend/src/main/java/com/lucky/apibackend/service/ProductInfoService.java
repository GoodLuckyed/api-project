package com.lucky.apibackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.apibackend.model.entity.ProductInfo;

/**
* @author ccc
* @description 针对表【product_info(产品信息表)】的数据库操作Service
* @createDate 2024-07-11 19:04:14
*/
public interface ProductInfoService extends IService<ProductInfo> {

    /**
     * 校验参数
     * @param productInfo
     * @param add
     */
    void validProductInfo(ProductInfo productInfo, boolean add);
}
