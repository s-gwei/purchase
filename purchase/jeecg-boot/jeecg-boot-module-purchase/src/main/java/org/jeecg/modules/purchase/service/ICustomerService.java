package org.jeecg.modules.purchase.service;

import org.jeecg.modules.purchase.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 客户表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
public interface ICustomerService extends IService<Customer> {

    List<Customer> queryList();
}
