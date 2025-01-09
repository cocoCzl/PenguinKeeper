package com.dollar.penguin.common.exception;

import lombok.Getter;

@Getter
public class WebException extends RuntimeException {

  // 请求参数异常
  public final static int PARAM_FAILED = -1001;

  private final int code;

  public WebException(int code, String message) {
    super(message);
    this.code = code;
  }
}
