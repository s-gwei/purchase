package org.jeecg.modules.purchase.controller;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Mobandaochu {
    public static TemplateExportParams getTemplateParams(String name){
        return new TemplateExportParams("D:\\a.xlsx");
    }

    public static Workbook putong() {
        TemplateExportParams params = getTemplateParams("putong");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cusName", "员工个人信息");
        map.put("date", "2020-07-13");
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        return workbook;
    }
    public static void main(String[] args) throws IOException {
        Workbook workbook = putong();
        File savefile = new File("D:");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:\\result.xlsx");
        workbook.write(fos);
        fos.close();
    }


}
