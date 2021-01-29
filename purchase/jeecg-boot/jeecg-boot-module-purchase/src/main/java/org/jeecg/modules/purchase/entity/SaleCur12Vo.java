package org.jeecg.modules.purchase.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleCur12Vo implements Serializable {

    public String month;

    public Double total;
}
