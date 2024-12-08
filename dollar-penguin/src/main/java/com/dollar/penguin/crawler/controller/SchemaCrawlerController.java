package com.dollar.penguin.crawler.controller;

import com.dollar.penguin.common.DataException;
import com.dollar.penguin.crawler.model.vo.AnalysisDataBaseVo;
import com.dollar.penguin.crawler.service.SchemaCrawlerService;
import com.dollar.penguin.util.Result;
import java.util.List;
import java.util.Set;
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

    @RequestMapping(value = "/getTables", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result getTables(@RequestBody AnalysisDataBaseVo analysisDataBaseVo) {
        try {
            if (log.isInfoEnabled()) {
                log.info("loadTables, analysisDataBaseVo:{}", analysisDataBaseVo);
            }
            // 调用异步方法
            Set<String> tables = schemaCrawlerService.getTables(analysisDataBaseVo);
            return Result.success(tables);
        } catch (Exception e) {
            log.error("loadTables error:{}", e.getMessage(), e);
            if (e instanceof DataException dataBaseMetaException) {
                return Result.failure(dataBaseMetaException.getCode(),
                        dataBaseMetaException.getMessage());
            }
            return Result.failure("[获取表异常]");
        }
    }
}
