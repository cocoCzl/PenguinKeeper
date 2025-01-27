package com.dollar.penguin.common.exception;

import com.dollar.penguin.util.Result;
import com.dollar.penguin.common.enumUtil.ResultCodeEnum;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 空指针异常的处理
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result<String> handleNullPointerException(NullPointerException e) {
        log.error(e.getMessage(), e);
        return Result.failure(
                StringUtils.isBlank(e.getMessage()) ? ResultCodeEnum.ERR_DB_LOG.message()
                        : e.getMessage());
    }

    /**
     * 数据库异常的处理
     */
    @ExceptionHandler(value = DataException.class)
    public Result<String> handleDataException(DataException e) {
        if (StringUtils.isBlank(e.getMessage())) {
            return Result.failure(ResultCodeEnum.ERR_DATA_BASE.code(),
                    ResultCodeEnum.ERR_DATA_BASE.message());
        } else {
            return Result.failure(e.getCode(), e.getMessage());
        }
    }

    @ExceptionHandler(value = SQLException.class)
    public Result<String> handleSqlException(SQLException e) {
        if (StringUtils.isBlank(e.getMessage())) {
            return Result.failure(ResultCodeEnum.ERR_DATA_BASE.code(),
                    ResultCodeEnum.ERR_DATA_BASE.message());
        } else {
            return Result.failure(ResultCodeEnum.ERR_DATA_BASE.code(), e.getMessage());
        }
    }

    /**
     * web异常的处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> handleWebException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        StringBuilder sb = new StringBuilder();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return Result.failure(WebException.PARAM_FAILED, message);
    }

    /**
     * web异常的处理
     */
    @ExceptionHandler(value = WebException.class)
    public Result<String> handleWebException(WebException e) {
        log.error(e.getMessage(), e);
        if (StringUtils.isBlank(e.getMessage())) {
            return Result.failure(ResultCodeEnum.ERR_DB_LOG.code(),
                    ResultCodeEnum.ERR_DB_LOG.message());
        } else {
            return Result.failure(e.getCode(), e.getMessage());
        }
    }

    /**
     * 未特别处理的异常的通用处理
     */
    @ExceptionHandler(value = Exception.class)
    public Result<String> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Result.failure(
                StringUtils.isBlank(ex.getMessage()) ? ResultCodeEnum.FAILURE.message()
                        : ex.getMessage());
    }
}
