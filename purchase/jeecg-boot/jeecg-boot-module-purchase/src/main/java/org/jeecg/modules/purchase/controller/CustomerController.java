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
import org.jeecg.modules.purchase.entity.Customer;
import org.jeecg.modules.purchase.service.ICustomerService;

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
 * @Description: 客户表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Api(tags="客户表")
@RestController
@RequestMapping("/purchase/customer")
@Slf4j
public class CustomerController extends JeecgController<Customer, ICustomerService> {
	@Autowired
	private ICustomerService customerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param customer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "客户表-分页列表查询")
	@ApiOperation(value="客户表-分页列表查询", notes="客户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Customer customer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Customer> queryWrapper = QueryGenerator.initQueryWrapper(customer, req.getParameterMap());
		Page<Customer> page = new Page<Customer>(pageNo, pageSize);
		IPage<Customer> pageList = customerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "客户表-列表查询")
	 @ApiOperation(value="客户表-列表查询", notes="客户表-列表查询")
	 @GetMapping(value = "/queryList")
	 public Result<?> queryList(Customer customer) {
		 List<Customer>  list = customerService.queryList();
		 return Result.OK(list);
	 }
	/**
	 *   添加
	 *
	 * @param customer
	 * @return
	 */
	@AutoLog(value = "客户表-添加")
	@ApiOperation(value="客户表-添加", notes="客户表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Customer customer) {
		customerService.save(customer);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param customer
	 * @return
	 */
	@AutoLog(value = "客户表-编辑")
	@ApiOperation(value="客户表-编辑", notes="客户表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Customer customer) {
		customerService.updateById(customer);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户表-通过id删除")
	@ApiOperation(value="客户表-通过id删除", notes="客户表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		customerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户表-批量删除")
	@ApiOperation(value="客户表-批量删除", notes="客户表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.customerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户表-通过id查询")
	@ApiOperation(value="客户表-通过id查询", notes="客户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Customer customer = customerService.getById(id);
		if(customer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(customer);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param customer
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Customer customer) {
        return super.exportXls(request, customer, Customer.class, "客户表");
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
        return super.importExcel(request, response, Customer.class);
    }

}
