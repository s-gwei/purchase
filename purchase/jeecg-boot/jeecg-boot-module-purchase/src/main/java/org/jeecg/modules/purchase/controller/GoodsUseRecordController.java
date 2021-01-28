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
 * @Description: 货品使用记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-27
 * @Version: V1.0
 */
@Api(tags="货品使用记录表")
@RestController
@RequestMapping("/purchase/goodsUseRecord")
@Slf4j
public class GoodsUseRecordController extends JeecgController<GoodsUseRecord, IGoodsUseRecordService> {
	@Autowired
	private IGoodsUseRecordService goodsUseRecordService;

	 @Autowired
	 private IStockService stockService;
	/**
	 * 分页列表查询
	 *
	 * @param goodsUseRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "货品使用记录表-分页列表查询")
	@ApiOperation(value="货品使用记录表-分页列表查询", notes="货品使用记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GoodsUseRecord goodsUseRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GoodsUseRecord> queryWrapper = QueryGenerator.initQueryWrapper(goodsUseRecord, req.getParameterMap());
		Page<GoodsUseRecord> page = new Page<GoodsUseRecord>(pageNo, pageSize);
		IPage<GoodsUseRecord> pageList = goodsUseRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param goodsUseRecord
	 * @return
	 */
	@AutoLog(value = "货品使用记录表-添加")
	@ApiOperation(value="货品使用记录表-添加", notes="货品使用记录表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GoodsUseRecord goodsUseRecord) {
		goodsUseRecordService.save(goodsUseRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param goodsUseRecord
	 * @return
	 */
	@AutoLog(value = "货品使用记录表-编辑")
	@ApiOperation(value="货品使用记录表-编辑", notes="货品使用记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GoodsUseRecord goodsUseRecord) {
		goodsUseRecordService.updateById(goodsUseRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "货品使用记录表-通过id删除")
	@ApiOperation(value="货品使用记录表-通过id删除", notes="货品使用记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		GoodsUseRecord goodsUseRecord = goodsUseRecordService.getById(id);
		Stock stock = stockService.getById(goodsUseRecord.getStockId());
		stock.setAccount(stock.getAccount()+goodsUseRecord.getAccount());
		stock.setTotalPrice(stock.getTotalPrice()+goodsUseRecord.getTotalPrice());
		stock.setUpdateTime(new Date());
		stock.setUpdateBy(goodsUseRecord.getCreateBy());
		stockService.updateById(stock);
		goodsUseRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "货品使用记录表-批量删除")
	@ApiOperation(value="货品使用记录表-批量删除", notes="货品使用记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.goodsUseRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "货品使用记录表-通过id查询")
	@ApiOperation(value="货品使用记录表-通过id查询", notes="货品使用记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GoodsUseRecord goodsUseRecord = goodsUseRecordService.getById(id);
		if(goodsUseRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(goodsUseRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param goodsUseRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GoodsUseRecord goodsUseRecord) {
        return super.exportXls(request, goodsUseRecord, GoodsUseRecord.class, "货品使用记录表");
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
        return super.importExcel(request, response, GoodsUseRecord.class);
    }

}
