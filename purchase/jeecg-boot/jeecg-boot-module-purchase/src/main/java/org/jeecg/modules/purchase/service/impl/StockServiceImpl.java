package org.jeecg.modules.purchase.service.impl;

import org.jeecg.modules.purchase.entity.Stock;
import org.jeecg.modules.purchase.mapper.StockMapper;
import org.jeecg.modules.purchase.service.IStockService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 库存表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

}
