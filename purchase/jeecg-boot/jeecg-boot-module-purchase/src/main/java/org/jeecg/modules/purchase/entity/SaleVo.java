package org.jeecg.modules.purchase.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleVo implements Serializable {

    public Double dailySales;//当日销售额

    public Double dailyProfit;//当日利润

    public Double salesMonth;//当月销售额

    public Double profitMonth;//当月利润


}
