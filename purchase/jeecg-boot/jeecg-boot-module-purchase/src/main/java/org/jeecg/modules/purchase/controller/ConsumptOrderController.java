package org.jeecg.modules.purchase.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.purchase.entity.ConsumptOrder;
import org.jeecg.modules.purchase.service.IConsumptOrderService;
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
 * @Description: 客户订单价格表
 * @Author: jeecg-boot
 * @Date:   2021-02-21
 * @Version: V1.0
 */
@Api(tags="客户订单价格表")
@RestController
@RequestMapping("/purchase/consumptOrder")
@Slf4j
public class ConsumptOrderController extends JeecgController<ConsumptOrder, IConsumptOrderService> {
	@Autowired
	private IConsumptOrderService consumptOrderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param consumptOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "客户订单价格表-分页列表查询")
	@ApiOperation(value="客户订单价格表-分页列表查询", notes="客户订单价格表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ConsumptOrder consumptOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ConsumptOrder> queryWrapper = QueryGenerator.initQueryWrapper(consumptOrder, req.getParameterMap());
		Page<ConsumptOrder> page = new Page<ConsumptOrder>(pageNo, pageSize);
		IPage<ConsumptOrder> pageList = consumptOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param consumptOrder
	 * @return
	 */
	@AutoLog(value = "客户订单价格表-添加")
	@ApiOperation(value="客户订单价格表-添加", notes="客户订单价格表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ConsumptOrder consumptOrder) {
		consumptOrderService.save(consumptOrder);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param consumptOrder
	 * @return
	 */
	@AutoLog(value = "客户订单价格表-编辑")
	@ApiOperation(value="客户订单价格表-编辑", notes="客户订单价格表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ConsumptOrder consumptOrder) {
		consumptOrderService.updateById(consumptOrder);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户订单价格表-通过id删除")
	@ApiOperation(value="客户订单价格表-通过id删除", notes="客户订单价格表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		consumptOrderService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户订单价格表-批量删除")
	@ApiOperation(value="客户订单价格表-批量删除", notes="客户订单价格表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.consumptOrderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户订单价格表-通过id查询")
	@ApiOperation(value="客户订单价格表-通过id查询", notes="客户订单价格表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ConsumptOrder consumptOrder = consumptOrderService.getById(id);
		if(consumptOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(consumptOrder);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param consumptOrder
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ConsumptOrder consumptOrder) {
        return super.exportXls(request, consumptOrder, ConsumptOrder.class, "客户订单价格表");
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
        return super.importExcel(request, response, ConsumptOrder.class);
    }

}
