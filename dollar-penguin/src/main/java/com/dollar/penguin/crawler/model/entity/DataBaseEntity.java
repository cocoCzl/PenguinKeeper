package com.dollar.penguin.crawler.model.entity;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class DataBaseEntity {

    private int id;
    private int dataBaseCode;
    private String dataBaseName;
    private String url;
    private String pwd;
    private String userName;
    private Timestamp createAt;
    private Timestamp updatedAt;
}
