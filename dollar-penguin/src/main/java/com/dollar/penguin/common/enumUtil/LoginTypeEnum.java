package com.dollar.penguin.common.enumUtil;

import java.util.Arrays;

public enum LoginTypeEnum {

    EMAIL_ADDRESS(1, "邮箱登录"),
    USER_NAME(2, "用户名登录"),
    NICK_NAME(3, "昵称登录"),
    USER_ID(4, "用户ID登录"),
    UNKNOWN(-99, "登录方式非法");

    private final int code;
    private final String message;

    LoginTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public static LoginTypeEnum parse(int code) {
        return Arrays.stream(LoginTypeEnum.values())
                .filter(loginTypeEnum -> loginTypeEnum.code == code).findFirst().orElse(UNKNOWN);
    }
}
