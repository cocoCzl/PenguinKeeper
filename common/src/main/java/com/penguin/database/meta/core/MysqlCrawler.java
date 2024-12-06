package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import schemacrawler.schema.Table;

import java.util.Set;

public class MysqlCrawler extends AbstractCatalogsCrawler implements DataBaseCrawler {

    @Override
    public Set<String> getTables(String schemaName, String regex, String connectionUrl, String user,
            String password) {
        return Set.of();
    }

    @Override
    public Table getTable(String schemaName, String tableName, String connectionUrl, String user, String password) {
        return null;
    }
}
