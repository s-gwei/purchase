package org.jeecg.modules.purchase.service.impl;

import org.jeecg.modules.purchase.entity.Sale;
import org.jeecg.modules.purchase.entity.SaleCur12Vo;
import org.jeecg.modules.purchase.entity.SaleVo;
import org.jeecg.modules.purchase.mapper.SaleMapper;
import org.jeecg.modules.purchase.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 出货订单表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Service
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements ISaleService {

    @Autowired
    SaleMapper saleMapper;
    @Override
    public SaleVo queryCurrentSale() {
        return saleMapper.queryCurrentSale();
    }

    @Override
    public List<SaleCur12Vo> queryCur12Total() {
        //删除以前视图
        saleMapper.dropView();
        //创建最忌12个月视图
        saleMapper.createView();
        return saleMapper.queryCur12Total();
    }
}
