package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import com.penguin.database.util.DBType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCatalogsCrawler implements DataBaseCrawler {

    protected List<String> systemSchemaList;

    protected List<String> getAllSchemas(Connection connection, DBType dbType) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getCatalogs();
        ArrayList<String> list = new ArrayList<>(20);
        while (resultSet.next()) {
            String schema = resultSet.getString(1);
            if (!isSystemSchema(schema, dbType)) {
                list.add(schema);
            }
        }
        return list;
    }

    private boolean isSystemSchema(String schema, DBType dbType) {
        if (dbType.isUppercase()) {
            return systemSchemaList.contains(schema.toUpperCase());
        } else {
            return systemSchemaList.contains(schema.toLowerCase());
        }
    }
}
