package com.penguin.database.meta.core;

import com.penguin.database.meta.DataBaseCrawler;
import com.penguin.database.util.DBType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSchemaCrawler extends AbstractCrawler implements DataBaseCrawler {

    @Override
    public List<String> getAllSchemasOrCatalogs(Connection connection, DBType dbType) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getSchemas();
        ArrayList<String> list = new ArrayList<>(20);
        while (resultSet.next()) {
            String schema = resultSet.getString(1);
            if (isSystemSchemas(schema, dbType)) {
                continue;
            }
            list.add(schema);
        }
        return list;
    }
}
