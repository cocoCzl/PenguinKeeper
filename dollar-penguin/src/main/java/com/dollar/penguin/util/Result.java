package com.dollar.penguin.util;

import com.dollar.penguin.common.enumUtil.ResultCodeEnum;
import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result() {
    }

    private Result(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.code();
        this.message = resultCodeEnum.message();
    }

    private Result(ResultCodeEnum resultCodeEnum, T data) {
        this.code = resultCodeEnum.code();
        this.message = resultCodeEnum.message();
        this.data = data;
    }

    private Result(int code) {
        this.code = code;
    }

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result<String> success() {
        return new Result<>(ResultCodeEnum.SUCCESS);
    }

    public boolean isSuccess() {
        return ResultCodeEnum.SUCCESS.code().equals(code);
    }

    public static <K> Result<K> success(K data) {
        return new Result<>(ResultCodeEnum.SUCCESS, data);
    }

    public static <K> Result<K> failure(ResultCodeEnum resultCodeEnum, K data) {
        return new Result<>(resultCodeEnum, data);
    }

    public static Result<String> failure(String message) {
        return new Result<>(ResultCodeEnum.FAILURE, message);
    }

    public static Result<String> failure(int resultCode, String message) {
        return new Result<>(resultCode, message);
    }
}
