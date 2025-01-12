package com.dollar.penguin.common.enumUtil;

import java.util.Arrays;

public enum LoginTypeEnum {

    EMAIL_ADDRESS(1, "邮箱登录", "email_address"),
    USER_NAME(2, "用户名登录", "user_name"),
    NICK_NAME(3, "昵称登录", "nick_name"),
    USER_ID(4, "用户ID登录", "user_id"),
    UNKNOWN(-99, "登录方式非法", "");

    private final int code;
    private final String message;
    private final String dataCode;

    LoginTypeEnum(int code, String message, String dataCode) {
        this.code = code;
        this.message = message;
        this.dataCode = dataCode;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public String dataCode() {
        return dataCode;
    }

    public static LoginTypeEnum parse(int code) {
        return Arrays.stream(LoginTypeEnum.values())
                .filter(loginTypeEnum -> loginTypeEnum.code == code).findFirst().orElse(UNKNOWN);
    }
}
