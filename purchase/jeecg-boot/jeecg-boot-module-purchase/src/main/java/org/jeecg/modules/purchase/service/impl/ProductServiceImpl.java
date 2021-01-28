package org.jeecg.modules.purchase.service.impl;

import org.jeecg.modules.purchase.entity.Product;
import org.jeecg.modules.purchase.mapper.ProductMapper;
import org.jeecg.modules.purchase.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public List<Product> queryList() {
        return productMapper.selectList(null);
    }
}
