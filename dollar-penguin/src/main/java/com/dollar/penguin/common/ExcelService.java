package com.dollar.penguin.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExcelService {

    private static final List<String> dataBaseConfigHeaders = Arrays.asList("数据库名",
            "数据库连接串", "数据库密码", "数据库用户名");

    public void dataBaseConfigExcel(OutputStream ous, String sheetName) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);
        initHeaders(hssfWorkbook, sheet, dataBaseConfigHeaders);
        hssfWorkbook.write(ous);
        ous.flush();
    }

    private void initHeaders(HSSFWorkbook wb, HSSFSheet mainSheet, List<String> headers) {
        //表头样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        //字体样式
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short) 10);
        style.setFont(fontStyle);
        //生成主内容
        HSSFRow rowFirst = mainSheet.createRow(0);//第一个sheet的第一行为标题
        //写标题
        for (int i = 0; i < headers.size(); i++) {
            HSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
            mainSheet.setColumnWidth(i, 5000); //设置每列的列宽
            cell.setCellStyle(style); //加样式
            cell.setCellValue(headers.get(i)); //往单元格里写数据
        }
    }

    /**
     * 数据写入excel
     */
    private void initData(HSSFSheet mainSheet, List<Map<String, Object>> data) {
        int index = 0;
        //循环数据
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = new HashMap<>(data.get(i));
            String tableNames = String.valueOf(map.get("table_names"));
            if (tableNames.contains("DEFAULT_QUEUE")) {
                continue;
            }
            //同时有多个tablenames的情况
            if (tableNames.contains(",")) {
                index = i + 1;
                String[] split = tableNames.split(",");
                String[] columns;
                for (int j = 0; j < split.length; j++) {
                    HSSFRow rows = mainSheet.createRow(index);
                    columns = split[j].split("\\.");
                    for (int k = 0; k < columns.length; k++) {
                        HSSFCell cell = rows.createCell(k);
                        cell.setCellValue(columns[k]);
                    }
                    if (j < split.length - 1) {
                        index++;
                    }
                }
            } else {
                if (i == 0) {
                    index = 1;
                } else {
                    index++;
                }
                HSSFRow rows = mainSheet.createRow(index);
                String[] split = tableNames.split("\\.");
                for (int j = 0; j < split.length; j++) {
                    HSSFCell cell = rows.createCell(j);
                    cell.setCellValue(split[j]);
                }
            }
        }
    }
}
