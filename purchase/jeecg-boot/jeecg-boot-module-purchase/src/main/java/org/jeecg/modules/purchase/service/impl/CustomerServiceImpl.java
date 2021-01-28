package org.jeecg.modules.purchase.service.impl;

import org.jeecg.modules.purchase.entity.Customer;
import org.jeecg.modules.purchase.mapper.CustomerMapper;
import org.jeecg.modules.purchase.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 客户表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper  customerMapper;

    @Override
    public List<Customer> queryList() {
        return customerMapper.selectList(null);
    }
}
