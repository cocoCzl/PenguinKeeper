package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import schemacrawler.inclusionrule.RegularExpressionInclusionRule;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
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
        LimitOptionsBuilder limitOptionsBuilder = LimitOptionsBuilder.builder()
                .includeSchemas(new RegularExpressionInclusionRule(scheme));
        if (StringUtils.isNotEmpty(regex)) {
            limitOptionsBuilder.tableNamePattern(regex);
        }
        final LoadOptionsBuilder loadOptionsBuilder = LoadOptionsBuilder.builder()
                // Set what details are required in the schema - this affects the
                // time taken to crawl the schema
                .withSchemaInfoLevel(SchemaInfoLevelBuilder.minimum());
        final SchemaCrawlerOptions schemaCrawlerOptions = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withLimitOptions(limitOptionsBuilder.toOptions())
                .withLoadOptions(loadOptionsBuilder.toOptions());
        Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource, schemaCrawlerOptions);
        Schema dbSchema = null;
        for (final Schema schema : catalog.getSchemas()) {
            String name = schema.getName();
            if (StringUtils.isEmpty(name)) {
                name = schema.getCatalogName();
            }
            if (name.equalsIgnoreCase(schemaName)) {
                dbSchema = schema;
                break;
            }
        }

        if (dbSchema != null) {
            for (final Table table : catalog.getTables(dbSchema)) {
                if (table instanceof View) {
                    //不需要加载视图
                    continue;
                }
                allTables.add(table.getName());
            }
        }
        return allTables;
    }
}
