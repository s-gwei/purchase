package org.jeecg.modules.purchase.service.impl;

import org.jeecg.modules.purchase.entity.Order;
import org.jeecg.modules.purchase.mapper.OrderMapper;
import org.jeecg.modules.purchase.service.IOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
