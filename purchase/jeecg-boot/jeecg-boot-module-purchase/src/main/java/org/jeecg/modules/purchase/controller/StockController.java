package org.jeecg.modules.purchase.controller;

import java.util.Arrays;
import java.util.Date;
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
import org.jeecg.modules.purchase.entity.GoodsUseRecord;
import org.jeecg.modules.purchase.entity.Stock;
import org.jeecg.modules.purchase.service.IGoodsUseRecordService;
import org.jeecg.modules.purchase.service.IStockService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 库存表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Api(tags="库存表")
@RestController
@RequestMapping("/purchase/stock")
@Slf4j
public class StockController extends JeecgController<Stock, IStockService> {
	@Autowired
	private IStockService stockService;

	 @Autowired
	 private IGoodsUseRecordService goodsUseRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param stock
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "库存表-分页列表查询")
	@ApiOperation(value="库存表-分页列表查询", notes="库存表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Stock stock,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Stock> queryWrapper = QueryGenerator.initQueryWrapper(stock, req.getParameterMap());
		Page<Stock> page = new Page<Stock>(pageNo, pageSize);
		IPage<Stock> pageList = stockService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param stock
	 * @return
	 */
	@AutoLog(value = "库存表-添加")
	@ApiOperation(value="库存表-添加", notes="库存表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Stock stock) {
		stockService.save(stock);
		return Result.OK("添加成功！");
	}

	 /**
	  *   消耗
	  *
	  * @param stock
	  * @return
	  */
	 @AutoLog(value = "库存表-消耗")
	 @ApiOperation(value="库存表-消耗", notes="库存表-消耗")
	 @PostMapping(value = "/reduce")
	 public Result<?> reduce(@RequestBody Stock stock) {
		 Stock st = stockService.getById(stock.getId());
		 st.setAccount(st.getAccount()-stock.getAccount());
		 st.setTotalPrice(st.getUnitPrice()*st.getAccount());
		 stockService.updateById(st);
		 GoodsUseRecord goodsUseRecord = new GoodsUseRecord();
		 goodsUseRecord.setStockId(st.getId());
		 goodsUseRecord.setName(st.getName());
		 goodsUseRecord.setCompany(st.getCompany());
		 goodsUseRecord.setAccount(stock.getAccount());
		 goodsUseRecord.setUnitPrice(st.getUnitPrice());
		 goodsUseRecord.setTotalPrice(st.getUnitPrice()*stock.getAccount());
		 goodsUseRecord.setCreateTime(new Date());
		 goodsUseRecordService.save(goodsUseRecord);
		 return Result.OK("添加成功！");
	 }
	/**
	 *  编辑
	 *
	 * @param stock
	 * @return
	 */
	@AutoLog(value = "库存表-编辑")
	@ApiOperation(value="库存表-编辑", notes="库存表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Stock stock) {
		stockService.updateById(stock);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存表-通过id删除")
	@ApiOperation(value="库存表-通过id删除", notes="库存表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		stockService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存表-批量删除")
	@ApiOperation(value="库存表-批量删除", notes="库存表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stockService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存表-通过id查询")
	@ApiOperation(value="库存表-通过id查询", notes="库存表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Stock stock = stockService.getById(id);
		if(stock==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(stock);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stock
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Stock stock) {
        return super.exportXls(request, stock, Stock.class, "库存表");
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
        return super.importExcel(request, response, Stock.class);
    }

}
