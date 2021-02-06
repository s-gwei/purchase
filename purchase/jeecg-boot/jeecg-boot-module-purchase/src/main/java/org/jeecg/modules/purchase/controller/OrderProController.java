package org.jeecg.modules.purchase.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.purchase.entity.OrderPro;
import org.jeecg.modules.purchase.entity.Stock;
import org.jeecg.modules.purchase.service.IOrderProService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.purchase.service.IStockService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 订货表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Api(tags="订货表")
@RestController
@RequestMapping("/purchase/orderPro")
@Slf4j
public class OrderProController extends JeecgController<OrderPro, IOrderProService> {
	@Autowired
	private IOrderProService orderProService;
	 @Autowired
	 private IStockService stockService;
	
	/**
	 * 分页列表查询
	 *
	 * @param orderPro
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "订货表-分页列表查询")
	@ApiOperation(value="订货表-分页列表查询", notes="订货表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OrderPro orderPro,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OrderPro> queryWrapper = QueryGenerator.initQueryWrapper(orderPro, req.getParameterMap());
		Page<OrderPro> page = new Page<OrderPro>(pageNo, pageSize);
		IPage<OrderPro> pageList = orderProService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param orderPro
	 * @return
	 */
	@AutoLog(value = "订货表-添加")
	@ApiOperation(value="订货表-添加", notes="订货表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrderPro orderPro) {
		orderProService.save(orderPro);
		Stock stock = stockService.getOne(Wrappers.<Stock>lambdaQuery().eq(Stock::getName, orderPro.getName()).eq(Stock::getUnitPrice, orderPro.getUnitPrice()),false);
		if(stock == null){
			stock = new Stock();
			stock.setName(orderPro.getName());
			stock.setCreateBy(orderPro.getCreateBy());
			stock.setCreateTime(orderPro.getCreateTime());
			stock.setCompany(orderPro.getCompany());
			stock.setUnitPrice(orderPro.getUnitPrice());
			stock.setAccount(orderPro.getAccount());
			stock.setTotalPrice(orderPro.getTotalPrice());
			stock.setUpdateTime(orderPro.getCreateTime());
			stock.setUpdateBy(orderPro.getCreateBy());
			stockService.save(stock);
		}else{
			stock.setUpdateBy(orderPro.getCreateBy());
			stock.setUpdateTime(orderPro.getCreateTime());
			stock.setAccount(stock.getAccount()+orderPro.getAccount());
			stock.setTotalPrice(stock.getTotalPrice()+orderPro.getTotalPrice());
			stockService.updateById(stock);
		}

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param orderPro
	 * @return
	 */
	@AutoLog(value = "订货表-编辑")
	@ApiOperation(value="订货表-编辑", notes="订货表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrderPro orderPro) {
		orderProService.updateById(orderPro);
		Stock stock = stockService.getOne(Wrappers.<Stock>lambdaQuery().
				eq(Stock::getName, orderPro.getName()).eq(Stock::getUnitPrice, orderPro.getUnitPrice()),false);
		stock.setUpdateBy(orderPro.getCreateBy());
		stock.setUpdateTime(orderPro.getCreateTime());
		stock.setAccount(stock.getAccount()+orderPro.getAccount());
		stock.setTotalPrice(stock.getTotalPrice()+orderPro.getTotalPrice());
		stockService.updateById(stock);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订货表-通过id删除")
	@ApiOperation(value="订货表-通过id删除", notes="订货表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		OrderPro orderPro = orderProService.getById(id);
		orderProService.removeById(id);
		Stock stock = stockService.getOne(Wrappers.<Stock>lambdaQuery().
				eq(Stock::getName, orderPro.getName()).eq(Stock::getUnitPrice, orderPro.getUnitPrice()),false);
		stock.setUpdateBy(orderPro.getCreateBy());
		stock.setUpdateTime(orderPro.getCreateTime());
		stock.setAccount(stock.getAccount()-orderPro.getAccount());
		stock.setTotalPrice(stock.getTotalPrice()-orderPro.getTotalPrice());
		stockService.updateById(stock);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订货表-批量删除")
	@ApiOperation(value="订货表-批量删除", notes="订货表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.orderProService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订货表-通过id查询")
	@ApiOperation(value="订货表-通过id查询", notes="订货表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OrderPro orderPro = orderProService.getById(id);
		if(orderPro==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(orderPro);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param orderPro
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OrderPro orderPro) {
        return super.exportXls(request, orderPro, OrderPro.class, "订货表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OrderPro.class);
    }

}
