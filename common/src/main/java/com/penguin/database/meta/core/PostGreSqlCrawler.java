package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import java.util.Arrays;
import java.util.Set;

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
        return Set.of();
    }
}
