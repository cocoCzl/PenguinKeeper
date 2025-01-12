package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import schemacrawler.inclusionrule.RegularExpressionInclusionRule;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.*;
import schemacrawler.tools.utility.SchemaCrawlerUtility;
import us.fatehi.utility.datasource.DatabaseConnectionSource;

public class PostGreSqlCrawler extends AbstractSchemaCrawler implements DataBaseCrawler {

    public PostGreSqlCrawler() {
        systemSchemaList = Arrays.asList("anydata", "aq$_agent", "aq$_descriptor", "aq$_reg_info",
                "dbms_alert",
                "dbms_application_info", "dbms_aq", "dbms_aqadm", "dbms_crypto", "dbms_lob",
                "dbms_lock", "dbms_mview",
                "dbms_obfuscation_toolkit", "dbms_output", "dbms_pipe", "dbms_profiler",
                "dbms_random", "dbms_redact",
                "dbms_rls", "dbms_rowid", "dbms_scheduler", "dbms_session", "dbms_sql",
                "dbms_utility", "information_schema",
                "msg_prop_t", "pg_catalog", "sys", "utl_encode", "utl_file", "utl_http", "utl_i18n",
                "utl_mail", "utl_raw",
                "utl_smtp", "utl_tcp", "utl_url", "public");
    }

    @Override
    public Set<String> getTables(String schemaName, String regex, String connectionUrl, String user,
            String password) {
        final DatabaseConnectionSource dataSource = getDataSource(connectionUrl, user, password);
        Set<String> allTables = new HashSet<>(1000);
        String scheme = schemaName.toLowerCase();
        // 配置限制选项,使用正则表达式包含指定的schema
        LimitOptionsBuilder limitOptionsBuilder = LimitOptionsBuilder.builder()
                .includeSchemas(new RegularExpressionInclusionRule(scheme));
        if (StringUtils.isNotEmpty(regex)) {
            limitOptionsBuilder.tableNamePattern(regex);
        }
        // 配置加载选项,设置最小的架构信息级别,这样在抓取时将减少获取的元数据细节,从而加快速度
        final LoadOptionsBuilder loadOptionsBuilder = LoadOptionsBuilder.builder()
                // Set what details are required in the schema - this affects the
                // time taken to crawl the schema
                .withSchemaInfoLevel(SchemaInfoLevelBuilder.minimum());
        // 将限制和加载选项组合起来，以便控制抓取过程
        final SchemaCrawlerOptions schemaCrawlerOptions = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withLimitOptions(limitOptionsBuilder.toOptions())
                .withLoadOptions(loadOptionsBuilder.toOptions());
        // 获取Catalog
        Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource, schemaCrawlerOptions);

        // 获取scheme
        Optional<Schema> dbSchema = catalog.getSchemas().stream().filter(schema -> {
            String name = schema.getName();
            return StringUtils.isEmpty(name) ? schema.getCatalogName().equalsIgnoreCase(schemaName)
                    : name.equalsIgnoreCase(schemaName);
        }).findFirst();
        dbSchema.ifPresent(schema -> {
            catalog.getTables(schema).stream()
                    .filter(table -> !(table instanceof View)) // 过滤掉视图
                    .map(Table::getName) // 获取表名
                    .forEach(allTables::add); // 添加表名到 allTables 集合
        });
        return allTables;
    }

    @Override
    public Table getTable(String schemaName, String tableName, String connectionUrl,
                          String user, String password) {
        LimitOptionsBuilder limitOptionsBuilder = getLimitOptionsBuilder(schemaName, tableName);
        final DatabaseConnectionSource dataSource = getDataSource(connectionUrl, user, password);
        // 将限制和加载选项组合起来，以便控制抓取过程
        SchemaCrawlerOptions schemaCrawlerOptions = getSchemaCrawlerOptions(limitOptionsBuilder);
        // 获取Catalog
        Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource, schemaCrawlerOptions);
        // 获取tables
        Collection<Table> tables = catalog.getTables();
        if (!tables.isEmpty()) {
            return tables.iterator().next();
        } else {
            return null;
        }
    }
}
