package com.lucky.apibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.mapper.ProductInfoMapper;
import com.lucky.apibackend.model.entity.ProductInfo;
import com.lucky.apibackend.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author ccc
* @description 针对表【product_info(产品信息表)】的数据库操作Service实现
* @createDate 2024-07-11 19:04:14
*/
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {

    /**
     * 校验参数
     * @param productInfo
     * @param add
     */
    @Override
    public void validProductInfo(ProductInfo productInfo, boolean add) {
        if (productInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = productInfo.getName();
        String description = productInfo.getDescription();
        Integer total = productInfo.getTotal();
        String productType = productInfo.getProductType();
        Integer addPoints = productInfo.getAddPoints();

        // 添加产品时，所有参数必须非空
        if (add){
            if (StringUtils.isAnyBlank(name)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (total < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "金额不能为负数");
        }
        if (addPoints < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "增加积分不能为负数");
        }
    }
}




