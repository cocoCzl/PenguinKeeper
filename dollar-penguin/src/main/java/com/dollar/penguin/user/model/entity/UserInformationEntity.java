package com.dollar.penguin.user.model.entity;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class UserInformationEntity {

    private int id;
    private String userId;
    private String userName;
    private String nickName;
    private String pwd;
    private String emailAddress;
    private Timestamp createAt;
    private Timestamp updatedAt;
}
