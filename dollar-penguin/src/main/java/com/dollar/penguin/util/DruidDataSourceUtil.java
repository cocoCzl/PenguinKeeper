package com.dollar.penguin.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.penguin.database.util.DBType;
import com.penguin.database.util.DataBaseGrammar;
import javax.sql.DataSource;

public class DruidDataSourceUtil {

    public static DataSource initDataSource(String url, String user, String password,
            DBType dbtype) {
        DruidDataSource dataSource = new DruidDataSource();
        //设置连接参数
        dataSource.setUrl(url);
        dataSource.setDriverClassName(dbtype.getDriverClass());
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setValidationQuery(DataBaseGrammar.getValidationQuery(dbtype));
        dataSource.setKeepAlive(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(300000);
        dataSource.setMinEvictableIdleTimeMillis(600000);
        dataSource.setInitialSize(3);
        dataSource.setMinIdle(3);
        dataSource.setMaxActive(100);
        dataSource.setName("DruidDBSource_" + dbtype.name());
        dataSource.setConnectionErrorRetryAttempts(10);
        return dataSource;
    }
}
