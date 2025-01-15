package com.penguin.database.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;

@Getter
public enum DBType {
    ORACLE(0, "oracle.jdbc.OracleDriver", "jdbc:oracle", true, true),
    POSTGRESQL(1, "org.postgresql.Driver", "jdbc:postgresql", false, true),
    MYSQL(2, "com.mysql.cj.jdbc.Driver", "jdbc:mysql", false, false),
    UNKNOWN(-99, null, "", false, false);

    private final int index;
    private final String driverClass;
    private final String driverPrefix;
    private final boolean uppercase;
    private final boolean supportSchema;

    DBType(int index, String driverClass, String driverPrefix, boolean uppercase, boolean supportSchema) {
        this.index = index;
        this.driverClass = driverClass;
        this.driverPrefix = driverPrefix;
        this.uppercase = uppercase;
        this.supportSchema = supportSchema;
    }

    private static final Map<String, DBType> map = new HashMap<>();

    static {
        Arrays.stream(DBType.values()).filter(dbType -> dbType != UNKNOWN)
                .forEach(dbType -> map.put(dbType.driverPrefix, dbType));
    }

    public static DBType recognizeDbType(String stack) {
        return Arrays.stream(DBType.values())
                .filter(dbType -> stack.toUpperCase().contains(dbType.name())).findFirst()
                .orElse(UNKNOWN);
    }

    public static DBType parse(int index) {
        return Arrays.stream(DBType.values()).filter(dbType -> dbType.getIndex() == index)
                .findFirst().orElse(UNKNOWN);
    }

    public static DBType getDBType(String jdbcUrl) {
        String finalJdbcUrl = jdbcUrl.toLowerCase().trim();
        if (finalJdbcUrl.contains("://")) {
            DBType dbType = map.get(finalJdbcUrl.substring(0, jdbcUrl.indexOf("://")));
            if (dbType != null) {
                return dbType;
            }
        }
        AtomicReference<DBType> dbType = new AtomicReference<>();
        map.forEach((prefix, type) -> {
            if (finalJdbcUrl.startsWith(prefix)) {
                dbType.set(type);
            }
        });
        DBType dbType1 = dbType.get();
        if (dbType1 == null || dbType1 == UNKNOWN) {
            throw new RuntimeException("unknown DBType, " + jdbcUrl);
        }
        return dbType1;
    }
}
