package com.penguin.database.meta;

import com.penguin.database.util.DBType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DataBaseCrawler {

    List<String> getAllSchemasOrCatalogs(Connection connection, DBType dbType) throws SQLException;

    Set<String> getTables(String schemaName, String regex, String connectionUrl, String user,
            String password);
}
