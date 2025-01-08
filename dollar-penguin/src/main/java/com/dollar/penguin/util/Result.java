package com.dollar.penguin.util;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result() {
    }

    private Result(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    private Result(ResultCode resultCode, T data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
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
        return new Result<>(ResultCode.SUCCESS);
    }

    public boolean isSuccess() {
        return ResultCode.SUCCESS.code().equals(code);
    }

    public static <K> Result<K> success(K data) {
        return new Result<>(ResultCode.SUCCESS, data);
    }

    public static <K> Result<K> failure(ResultCode resultCode, K data) {
        return new Result<>(resultCode, data);
    }

    public static Result<String> failure(String message) {
        return new Result<>(ResultCode.FAILURE, message);
    }

    public static Result<String> failure(int resultCode, String message) {
        return new Result<>(resultCode, message);
    }
}
