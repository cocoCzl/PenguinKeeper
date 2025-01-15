package com.penguin.database.util;

public class DataBaseGrammar {

    public static String getValidationQuery(DBType dbtype) {
        return switch (dbtype) {
            case POSTGRESQL -> "SELECT 1 ";
            default -> "SELECT 1 FROM DUAL";
        };
    }
}
