package org.jeecg.modules.purchase.controller;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Mobandaochu {

    public static TemplateExportParams getTemplateParams(){
//        return new TemplateExportParams("D:\\test\\1.xlsx");
        return new TemplateExportParams("/templates/1.xlsx");
    }

    public static Workbook putong() {
        TemplateExportParams params = getTemplateParams();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "员工个人信息");
        map.put("name", "张三");
        map.put("age", "23");
         Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        return workbook;
    }
    public static void main(String[] args) throws IOException {
////        File file = new File("D:\\test\\1.xlsx");
//        Mobandaochu Mobandaochu = new Mobandaochu();
//        Mobandaochu.readExcel(file);
        Workbook workbook = putong();
        File savefile = new File("D:");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:\\result.xlsx");
        workbook.write(fos);
        fos.close();
    }
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public void readExcel(File file) {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
//            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
//            int sheet_size = wb.getNumberOfSheets();
//            for (int index = 0; index < sheet_size; index++) {
//                // 每个页签创建一个Sheet对象
//                Sheet sheet = wb.getSheet(index);
//                // sheet.getRows()返回该页的总行数
//                for (int i = 0; i < sheet.getRows(); i++) {
//                    // sheet.getColumns()返回该页的总列数
//                    for (int j = 0; j < sheet.getColumns(); j++) {
//                        String cellinfo = sheet.getCell(j, i).getContents();
//                        System.out.println(cellinfo);
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
