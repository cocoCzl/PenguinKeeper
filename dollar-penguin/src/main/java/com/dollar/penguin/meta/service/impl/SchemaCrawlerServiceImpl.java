package com.dollar.penguin.meta.service.impl;

import com.dollar.penguin.common.DataException;
import com.dollar.penguin.meta.mapper.MetaMapper;
import com.dollar.penguin.meta.model.entity.DataBaseEntity;
import com.dollar.penguin.meta.model.entity.TableInfoEntity;
import com.dollar.penguin.meta.model.vo.AnalysisDataBaseVo;
import com.dollar.penguin.meta.service.SchemaCrawlerService;
import com.dollar.penguin.util.TEAUtil;
import com.penguin.database.meta.DataBaseCrawler;
import com.penguin.database.meta.DataBaseCrawlerFactory;
import com.penguin.database.util.DBType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SchemaCrawlerServiceImpl implements SchemaCrawlerService {

    @Autowired
    private MetaMapper metaMapper;

    @Override
    public List<String> getAllSchemasOrCateLogs(int databaseId) throws SQLException {
        DataBaseEntity dataBaseEntity = metaMapper.queryDataBaseById(databaseId);
        DBType dbType = DBType.getDBType(dataBaseEntity.getUrl());
        DataBaseCrawler dataBaseCrawler = DataBaseCrawlerFactory.createDataBaseCrawler(dbType);
        Connection connection = null;
        try {
            Class.forName(dbType.getDriverClass());
            String pwd = TEAUtil.decode(dataBaseEntity.getPwd());
            connection = DriverManager.getConnection(dataBaseEntity.getUrl(), dataBaseEntity.getUserName(), pwd);
            // 解析数据获取所有Schema或者CateLog
            List<String> rsList = dataBaseCrawler.getAllSchemasOrCatalogs(connection, dbType);
            if (log.isDebugEnabled()) {
                if (dbType.isSupportSchema()) {
                    log.debug("All Schemas:{}", rsList);
                } else {
                    log.debug("All CateLogs:{}", rsList);
                }
            }
            return rsList;
        } catch (Exception e) {
            log.error("get Schemas or CateLogs error:{}", e.getMessage(), e);
            throw new DataException(DataException.CRAWLER_FAILED, "get Schemas or CateLogs error!");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    @Transactional
    @Async("async-executor")
    public CompletableFuture<Boolean> insertTables(AnalysisDataBaseVo analysisDataBaseVo) {
        DataBaseEntity dataBaseEntity = metaMapper.queryDataBaseById(
                analysisDataBaseVo.getDatabaseId());
        DBType dbType = DBType.getDBType(dataBaseEntity.getUrl());
        // 解析获取tables
        DataBaseCrawler dataBaseCrawler = DataBaseCrawlerFactory.createDataBaseCrawler(dbType);
        String pwd = TEAUtil.decode(dataBaseEntity.getPwd());
        Set<String> tables = dataBaseCrawler.getTables(analysisDataBaseVo.getSchemaName(),
                analysisDataBaseVo.getRegex(), dataBaseEntity.getUrl(),
                dataBaseEntity.getUserName(), pwd);
        boolean success = false;
        if (!tables.isEmpty()) {
            List<TableInfoEntity> tableInfoList = buildTableInfoList(dataBaseEntity.getDataBaseCode(),
                    analysisDataBaseVo.getSchemaName(), tables);
            if (log.isDebugEnabled()) {
                log.debug("{},All Tables:{}", analysisDataBaseVo.getSchemaName(), tableInfoList);
            }
            metaMapper.batchInsertTable(tableInfoList);
            success = true;
        }
        // 返回 CompletableFuture 对象
        return CompletableFuture.completedFuture(success);
    }

    private List<TableInfoEntity> buildTableInfoList(int dataBaseCode, String schemaName,
            Set<String> tables) {
        List<TableInfoEntity> tableInfoList = new ArrayList<>();
        tables.forEach(table -> {
            TableInfoEntity tableInfo = new TableInfoEntity();
            tableInfo.setTableName(table);
            tableInfo.setSchemaName(schemaName);
            tableInfo.setDataBaseCode(dataBaseCode);
            tableInfoList.add(tableInfo);
        });
        return tableInfoList;
    }
}
