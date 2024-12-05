package com.dollar.penguin.common;

import lombok.Getter;

@Getter
public class DataException extends RuntimeException{

    // 新增数据库信息参数异常
    public final static int DATA_BASE_PARAMETER_FAILED = -2001;

    // 数据插入异常
    public final static int DATA_INSERT_FAILED = -2002;

    // 数据查询异常
    public final static int DATA_SELECT_FAILED = -2003;

    public final static int CRAWLER_FAILED = -2005;

    private final int code;

    public DataException(int code, String message) {
        super(message);
        this.code = code;
    }
}
