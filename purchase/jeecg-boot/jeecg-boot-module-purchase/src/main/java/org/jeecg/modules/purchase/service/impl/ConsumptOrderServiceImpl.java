package org.jeecg.modules.purchase.service.impl;


import org.jeecg.modules.purchase.entity.ConsumptOrder;
import org.jeecg.modules.purchase.mapper.ConsumptOrderMapper;
import org.jeecg.modules.purchase.service.IConsumptOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 客户订单价格表
 * @Author: jeecg-boot
 * @Date:   2021-02-21
 * @Version: V1.0
 */
@Service
public class ConsumptOrderServiceImpl extends ServiceImpl<ConsumptOrderMapper, ConsumptOrder> implements IConsumptOrderService {

    @Override
    public boolean save(ConsumptOrder entity) {
        return false;
    }
}
