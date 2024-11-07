package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import com.penguin.database.util.DBType;
import java.util.List;
import us.fatehi.utility.datasource.DatabaseConnectionSource;
import us.fatehi.utility.datasource.DatabaseConnectionSources;
import us.fatehi.utility.datasource.MultiUseUserCredentials;

public abstract class AbstractCrawler implements DataBaseCrawler {

    protected List<String> systemSchemaList;

    protected boolean isSystemSchemas(String schema, DBType dbType) {
        if (dbType.isUppercase()) {
            return systemSchemaList.contains(schema.toUpperCase());
        } else {
            return systemSchemaList.contains(schema.toLowerCase());
        }
    }

    protected DatabaseConnectionSource getDataSource(String connectionUrl, String user,
            String password) {
        return DatabaseConnectionSources.newDatabaseConnectionSource(connectionUrl,
                new MultiUseUserCredentials(user, password));
    }
}
