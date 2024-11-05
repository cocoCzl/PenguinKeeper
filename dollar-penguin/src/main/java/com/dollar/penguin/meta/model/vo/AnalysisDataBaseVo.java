package com.dollar.penguin.meta.model.vo;

import lombok.Data;

@Data
public class AnalysisDataBaseVo {
    private int databaseId;
    private String schemaName;
    // 正则表达式，用于定义想要匹配的表名模式
    private String regex;
}
