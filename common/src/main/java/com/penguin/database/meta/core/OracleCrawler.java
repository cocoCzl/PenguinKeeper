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

    @Override
    public Set<String> getTables(String schemaName, String regex, String connectionUrl, String user,
            String password) {
        if (log.isInfoEnabled()) {
            log.debug("schemaName:{}, regex:{}, connectionUrl:{}", schemaName, regex, connectionUrl);
        }
        final DatabaseConnectionSource dataSource = getDataSource(connectionUrl, user, password);
        Set<String> allTables = new HashSet<>(1000);
        String scheme = schemaName.toUpperCase();
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
        // 指定数据检索的选项
        final SchemaRetrievalOptions schemaRetrievalOptions = SchemaRetrievalOptionsBuilder.newSchemaRetrievalOptions();
        // 获取Catalog
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
