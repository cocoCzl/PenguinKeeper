package com.dollar.penguin.meta.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DataBaseConfigTemplatePojo {

    @ExcelProperty("数据库名")
    private String dataBaseName;

    @ExcelProperty("数据库连接串")
    private String url;

    @ExcelProperty("数据库密码")
    private String pwd;

    @ExcelProperty("数据库用户名")
    private String userName;

}
