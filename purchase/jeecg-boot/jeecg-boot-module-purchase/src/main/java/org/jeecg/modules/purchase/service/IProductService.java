package org.jeecg.modules.purchase.service;

import org.jeecg.modules.purchase.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
public interface IProductService extends IService<Product> {

    List<Product> queryList();
}
