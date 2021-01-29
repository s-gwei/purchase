package org.jeecg.modules.purchase.service;

import org.jeecg.modules.purchase.entity.Sale;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.purchase.entity.SaleCur12Vo;
import org.jeecg.modules.purchase.entity.SaleVo;

import java.util.List;

/**
 * @Description: 出货订单表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
public interface ISaleService extends IService<Sale> {

    SaleVo queryCurrentSale();

    List<SaleCur12Vo> queryCur12Total();
}
