package org.jeecg.modules.purchase.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.Sale;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.purchase.entity.SaleCur12Vo;
import org.jeecg.modules.purchase.entity.SaleVo;
import org.springframework.stereotype.Repository;

/**
 * @Description: 出货订单表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Repository
public interface SaleMapper extends BaseMapper<Sale> {

    SaleVo queryCurrentSale();

    void createView();

    List<SaleCur12Vo> queryCur12Total();

    void dropView();

    List<SaleCur12Vo> queryCur12TotalProfit();
}
