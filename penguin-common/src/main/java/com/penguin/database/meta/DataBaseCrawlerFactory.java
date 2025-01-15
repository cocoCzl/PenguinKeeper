package com.penguin.database.meta;

import com.penguin.database.meta.core.MysqlCrawler;
import com.penguin.database.meta.core.OracleCrawler;
import com.penguin.database.meta.core.PostGreSqlCrawler;
import com.penguin.database.util.DBType;

public class DataBaseCrawlerFactory {

    public static DataBaseCrawler createDataBaseCrawler(DBType dbType) {
        return switch (dbType) {
            case ORACLE -> new OracleCrawler();
            case POSTGRESQL -> new PostGreSqlCrawler();
            case MYSQL -> new MysqlCrawler();
            default -> throw new RuntimeException("Unknown database type!");
        };
    }
}

