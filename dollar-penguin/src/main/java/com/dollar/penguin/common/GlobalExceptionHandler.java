package com.dollar.penguin.common;

import com.dollar.penguin.util.Result;
import com.dollar.penguin.util.ResultCode;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        return Result.failure(StringUtils.isBlank(e.getMessage()) ? ResultCode.ERR_DB_LOG.message()
                : e.getMessage());
    }

    /**
     * 数据库异常的处理
     */
    @ExceptionHandler(value = DataException.class)
    public Result<String> handleDataException(DataException e) {
        if (StringUtils.isBlank(e.getMessage())) {
            return Result.failure(ResultCode.ERR_DATA_BASE.code(), ResultCode.ERR_DATA_BASE.message());
        } else {
            return Result.failure(e.getCode(), e.getMessage());
        }
    }

    @ExceptionHandler(value = SQLException.class)
    public Result<String> handleSqlException(SQLException e) {
        if (StringUtils.isBlank(e.getMessage())) {
            return Result.failure(ResultCode.ERR_DATA_BASE.code(), ResultCode.ERR_DATA_BASE.message());
        } else {
            return Result.failure(ResultCode.ERR_DATA_BASE.code(), e.getMessage());
        }
    }

    /**
     * 未特别处理的异常的通用处理
     */
    @ExceptionHandler(value = Exception.class)
    public Result<String> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Result.failure(StringUtils.isBlank(ex.getMessage()) ? ResultCode.FAILURE.message()
                : ex.getMessage());
    }
}
