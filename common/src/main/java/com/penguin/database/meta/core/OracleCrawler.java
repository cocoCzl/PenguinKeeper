package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
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
import schemacrawler.schemacrawler.SchemaRetrievalOptions;
import schemacrawler.schemacrawler.SchemaRetrievalOptionsBuilder;
import schemacrawler.tools.options.Config;
import schemacrawler.tools.utility.SchemaCrawlerUtility;
import us.fatehi.utility.datasource.DatabaseConnectionSource;

@Slf4j
public class OracleCrawler extends AbstractSchemaCrawler implements DataBaseCrawler {

    public OracleCrawler() {
        systemSchemaList = Arrays.asList("ANONYMOUS", "APPQOSSYS", "AUDSYS", "CTXSYS", "DBSFWUSER",
                "DBSNMP", "DIP", "DVF", "DVSYS",
                "GGSYS", "GSMADMIN_INTERNAL", "GSMCATUSER", "GSMROOTUSER", "GSMUSER",
                "LBACSYS", "MDDATA", "MDSYS", "OJVMSYS", "OLAPSYS", "ORACLE_OCM", "ORDDATA",
                "ORDPLUGINS",
                "ORDSYS", "OUTLN", "REMOTE_SCHEDULER_AGENT", "SI_INFORMTN_SCHEMA",
                "SYS", "SYS$UMF", "SYSBACKUP", "SYSDG", "SYSKM", "SYSRAC", "SYSTEM", "WMSYS", "XDB",
                "XS$NULL");
    }

    public Set<String> analysisTables(String schemaName, String regex, DatabaseConnectionSource dataSource) {
        if (log.isInfoEnabled()) {
            log.debug("schemaName:{}, regex:{}", schemaName, regex);
        }
        Set<String> allTables = new HashSet<>(1000);
        String scheme = schemaName.toUpperCase();
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
        final SchemaRetrievalOptions schemaRetrievalOptions = SchemaRetrievalOptionsBuilder.newSchemaRetrievalOptions();
        Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource, schemaRetrievalOptions,
                schemaCrawlerOptions, new Config());

        Schema dbSchema = null;
        for (final Schema schema : catalog.getSchemas()) {
            String name = schema.getName();
            if (StringUtils.isEmpty(name)) {
                name = schema.getCatalogName();
            }
            if (name.equalsIgnoreCase(schemaName)) {
                if (log.isDebugEnabled()) {
                    log.debug("name:{}, schemaName:{}", name, schemaName);
                }
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
        } else {
            if (log.isInfoEnabled()) {
                log.info("dbSchema is null, allTables:{}", allTables);
            }
        }
        return allTables;
    }
}
