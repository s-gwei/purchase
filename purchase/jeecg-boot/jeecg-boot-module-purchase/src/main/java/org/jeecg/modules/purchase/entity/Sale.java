package org.jeecg.modules.purchase.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 出货订单表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Data
@TableName("sale")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sale对象", description="出货订单表")
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;

	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**客户店名*/
    @Excel(name = "客户店名", width = 15)
    @ApiModelProperty(value = "客户店名")
    private java.lang.String customerName;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称")
    private java.lang.String productName;
	/**产品id*/
//	@Excel(name = "产品id", width = 15)
    @ApiModelProperty(value = "产品id")
    private java.lang.String productId;
	/**单位*/
	@Excel(name = "单位", width = 8)
    @ApiModelProperty(value = "单位")
    private java.lang.String company;
	/**单价*/
	@Excel(name = "单价(元)", width = 8)
    @ApiModelProperty(value = "单价")
    private java.lang.Double unitPrice;
	/**数量*/
	@Excel(name = "数量", width = 8)
    @ApiModelProperty(value = "数量")
    private java.lang.Double account;
	/**总价*/
	@Excel(name = "总价(元)", width = 8)
    @ApiModelProperty(value = "总价")
    private java.lang.Double totalPrice;
	/**客户id*/
//	@Excel(name = "客户id", width = 15)
    @ApiModelProperty(value = "客户id")
    private java.lang.String customerId;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
//    @Excel(name = "创建时间", width = 30)
    private java.util.Date createTime;

}
