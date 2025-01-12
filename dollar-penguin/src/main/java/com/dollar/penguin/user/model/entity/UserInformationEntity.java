package com.dollar.penguin.user.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class UserInformationEntity {

    private int id;
    private String userId;
    private String userName;
    private String nickName;
    @JsonIgnore
    private String pwd;
    private String emailAddress;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
