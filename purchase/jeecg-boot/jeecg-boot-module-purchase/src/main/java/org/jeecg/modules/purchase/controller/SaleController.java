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

import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.purchase.entity.*;
import org.jeecg.modules.purchase.service.ICustomerService;
import org.jeecg.modules.purchase.service.IProductService;
import org.jeecg.modules.purchase.service.ISaleService;

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
 * @Description: 出货订单表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Api(tags="出货订单表")
@RestController
@RequestMapping("/purchase/sale")
@Slf4j
public class SaleController extends JeecgController<Sale, ISaleService> {
	@Autowired
	private ISaleService saleService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ICustomerService customerService;


	
	/**
	 * 分页列表查询
	 *
	 * @param sale
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "出货订单表-分页列表查询")
	@ApiOperation(value="出货订单表-分页列表查询", notes="出货订单表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Sale sale,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Sale> queryWrapper = QueryGenerator.initQueryWrapper(sale, req.getParameterMap());
		Page<Sale> page = new Page<Sale>(pageNo, pageSize);
		IPage<Sale> pageList = saleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sale
	 * @return
	 */
	@AutoLog(value = "出货订单表-添加")
	@ApiOperation(value="出货订单表-添加", notes="出货订单表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Sale sale) {
		Product product = productService.getById(sale.getProductId());
		sale.setProductName(product.getName());
		sale.setCompany("/斤");
		Customer customer = customerService.getById(sale.getCustomerId());
		sale.setCustomerName(customer.getShopName());
		saleService.save(sale);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sale
	 * @return
	 */
	@AutoLog(value = "出货订单表-编辑")
	@ApiOperation(value="出货订单表-编辑", notes="出货订单表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Sale sale) {
		saleService.updateById(sale);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "出货订单表-通过id删除")
	@ApiOperation(value="出货订单表-通过id删除", notes="出货订单表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		saleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "出货订单表-批量删除")
	@ApiOperation(value="出货订单表-批量删除", notes="出货订单表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.saleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "出货订单表-通过id查询")
	@ApiOperation(value="出货订单表-通过id查询", notes="出货订单表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Sale sale = saleService.getById(id);
		if(sale==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sale);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sale
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Sale sale) {
		// Step.1 组装查询条件
		QueryWrapper<Sale> queryWrapper = QueryGenerator.initQueryWrapper(sale, request.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		// Step.2 获取导出数据
		List<Sale> exportList = saleService.list(queryWrapper);

		// 过滤选中数据
		Double total = 0d;
		for(Sale sales : exportList){
			total+=sales.getTotalPrice() == null?0:sales.getTotalPrice();
		}

        return super.exportXls(request, sale, Sale.class, "武汉泓强食品有限责任公司,"+total);

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
        return super.importExcel(request, response, Sale.class);
    }

	 /**
	  * 查询本日销售额
	  *
	  * @return
	  */
	 @AutoLog(value = "出货订单表-查询")
	 @ApiOperation(value="出货订单表-查询", notes="出货订单表-查询")
	 @GetMapping(value = "/querySaleInfo")
	 public Result<?> queryCurrentSale() {
		 SaleVo saleVo = saleService.queryCurrentSale();
		 //计算毛利润
		 Double dailySales = saleVo.getDailySales() == null ? 0 :saleVo.getDailySales();
		 saleVo.setDailySales(dailySales);
		 Double dailyProfit = saleVo.getDailyProfit() == null ? 0 :saleVo.getDailyProfit();
		 saleVo.setDailyProfit(dailySales - dailyProfit);
		 Double salesMonth = saleVo.getSalesMonth() == null ? 0 :saleVo.getSalesMonth();
		 saleVo.setSalesMonth(salesMonth);
		 Double profitMonth = saleVo.getProfitMonth() == null ? 0:saleVo.getProfitMonth();
		 saleVo.setProfitMonth(salesMonth - profitMonth);
		 return Result.OK(saleVo);
	 }

	 /**
	  * 查询最近12个月销售数据
	  *
	  * @return
	  */
	 @AutoLog(value = "出货订单表-最近12个月销售数据")
	 @ApiOperation(value="出货订单表-最近12个月销售数据", notes="出货订单表-最近12个月销售数据")
	 @GetMapping(value = "/queryCur12Total")
	 public Result<?> queryCur12Total() {
		 List<SaleCur12Vo> saleCur12Vo = saleService.queryCur12Total();
		 //计算毛利润
		 return Result.OK(saleCur12Vo);
	 }


 }
