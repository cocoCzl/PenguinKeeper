package com.dollar.penguin.util;

public enum ResultCode {
    SUCCESS(1, "成功"),
    FAILURE(-1, "运行异常"),
    PARAM_INVALID(1001, "参数无效"),
    ERR_DATA_BASE(1002, "数据库异常"),
    ERR_DB_LOG(500, "内部错。请联系运维人员");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return code;
    }

    public String message() {
        return message;
    }

}