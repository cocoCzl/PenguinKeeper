package com.dollar.penguin.meta.controller;

import com.dollar.penguin.common.DataException;
import com.dollar.penguin.meta.model.vo.AnalysisDataBaseVo;
import com.dollar.penguin.meta.service.SchemaCrawlerService;
import com.dollar.penguin.util.Result;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/meta/schema")
public class SchemaCrawlerController {

    @Autowired
    SchemaCrawlerService schemaCrawlerService;

    @RequestMapping(value = "/querySchemas", method = RequestMethod.GET)
    public Result getAllSchemasOrCateLogs(
            @RequestParam(value = "databaseId", required = false) Integer databaseId) {
        try {
            List<String> schemaList = schemaCrawlerService.getAllSchemasOrCateLogs(databaseId);
            return Result.success(schemaList);
        } catch (Throwable e) {
            log.error("get Schemas Or CateLogs error:{}", e.getMessage(), e);
            if (e instanceof DataException dataBaseMetaException) {
                return Result.failure(dataBaseMetaException.getCode(),
                        dataBaseMetaException.getMessage());
            }
            return Result.failure("[获取所有数据库模式异常]");
        }
    }

    @RequestMapping(value = "/loadTables", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result loadTables(@RequestBody AnalysisDataBaseVo analysisDataBaseVo) {
        try {
            if (log.isInfoEnabled()) {
                log.info("loadTables, analysisDataBaseVo:{}", analysisDataBaseVo);
            }
            // 调用异步方法
            CompletableFuture<Boolean> rs = schemaCrawlerService.insertTables(analysisDataBaseVo);
            rs.thenAccept(success -> {
                if (success) {
                    log.info("Tables loaded successfully");
                } else {
                    log.error("Tables loading failed");
                }
            });
            return Result.success();
        } catch (Exception e) {
            log.error("loadTables error:{}", e.getMessage(), e);
            if (e instanceof DataException dataBaseMetaException) {
                return Result.failure(dataBaseMetaException.getCode(),
                        dataBaseMetaException.getMessage());
            }
            return Result.failure("[加载所有表异常]");
        }
    }
}
