package com.dollar.penguin.meta.service.impl;

import com.dollar.penguin.common.DataException;
import com.dollar.penguin.meta.mapper.MetaMapper;
import com.dollar.penguin.meta.model.DataBaseEntity;
import com.dollar.penguin.meta.model.vo.AnalysisDataBaseVo;
import com.dollar.penguin.meta.service.SchemaCrawlerService;
import com.dollar.penguin.util.TEAUtil;
import com.penguin.database.meta.DataBaseCrawler;
import com.penguin.database.meta.DataBaseCrawlerFactory;
import com.penguin.database.util.DBType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            List<String> rsList = dataBaseCrawler.getAllSchemasOrCatalogs(connection, dbType);
            if (log.isInfoEnabled()) {
                if (dbType.isSupportSchema()) {
                    log.info("All Schemas:{}", rsList);
                } else {
                    log.info("All CateLogs:{}", rsList);
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
    public Set<String> getTables(AnalysisDataBaseVo analysisDataBaseVo) {
        DataBaseEntity dataBaseEntity = metaMapper.queryDataBaseById(
                analysisDataBaseVo.getDatabaseId());
        DBType dbType = DBType.getDBType(dataBaseEntity.getUrl());
        DataBaseCrawler dataBaseCrawler = DataBaseCrawlerFactory.createDataBaseCrawler(dbType);
        String pwd = TEAUtil.decode(dataBaseEntity.getPwd());
        Set<String> tables = dataBaseCrawler.getTables(analysisDataBaseVo.getSchemaName(),
                analysisDataBaseVo.getRegex(), dataBaseEntity.getUrl(),
                dataBaseEntity.getUserName(), pwd);
        if (log.isInfoEnabled()) {
            log.info("{},All Tables:{}", analysisDataBaseVo.getSchemaName(), tables);
        }
        return tables;
    }

}
