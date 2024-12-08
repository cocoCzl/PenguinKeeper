package com.dollar.penguin.crawler.service;

import com.dollar.penguin.crawler.model.vo.AnalysisDataBaseVo;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface SchemaCrawlerService {

    List<String> getAllSchemasOrCateLogs(int databaseId) throws SQLException;

    Set<String> getTables(AnalysisDataBaseVo analysisDataBaseVo);
}
