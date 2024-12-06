package com.penguin.database.meta;

import com.penguin.database.util.DBType;
import schemacrawler.schema.Table;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DataBaseCrawler {

    List<String> getAllSchemasOrCatalogs(Connection connection, DBType dbType) throws SQLException;

    Set<String> getTables(String schemaName, String regex, String connectionUrl, String user,
            String password);

    Table getTable(String schemaName, String tableName, String connectionUrl, String user, String password);
}
