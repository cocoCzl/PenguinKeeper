package com.dollar.penguin.meta.model.entity;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class TableInfoEntity {

    private int id;
    private int dataBaseCode;
    private String schemaName;
    private String tableName;
    private Timestamp createAt;
    private Timestamp updatedAt;
}
