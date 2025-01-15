package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import com.penguin.database.util.DBType;
import java.util.List;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
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

    protected SchemaCrawlerOptions getSchemaCrawlerOptions(LimitOptionsBuilder limitOptionsBuilder) {
        // 过滤外键
        final SchemaInfoLevelBuilder schemaInfoLevelBuilder = SchemaInfoLevelBuilder.builder()
                .setRetrieveColumnDataTypes(true)
                .setRetrieveIndexes(true)
                .setRetrieveRoutineParameters(true)
                .setRetrieveTableColumns(true)
                .setRetrieveTables(true)
                .setRetrieveRoutines(true)
                .setRetrieveDatabaseInfo(true)
                .setRetrieveForeignKeys(false);
        final LoadOptionsBuilder loadOptionsBuilder = LoadOptionsBuilder.builder()
                // Set what details are required in the schema - this affects the
                // time taken to crawl the schema
                .withSchemaInfoLevelBuilder(schemaInfoLevelBuilder);
        return SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withLimitOptions(limitOptionsBuilder.toOptions())
                .withLoadOptions(loadOptionsBuilder.toOptions());
    }
}
