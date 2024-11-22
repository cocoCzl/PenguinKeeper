package com.dollar.penguin.meta.service;

import com.dollar.penguin.meta.model.vo.AnalysisDataBaseVo;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SchemaCrawlerService {

    List<String> getAllSchemasOrCateLogs(int databaseId) throws SQLException;

    CompletableFuture<Boolean> insertTables(AnalysisDataBaseVo analysisDataBaseVo);
}
