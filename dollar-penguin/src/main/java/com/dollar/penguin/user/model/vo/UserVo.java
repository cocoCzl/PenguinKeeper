package com.dollar.penguin.user.model.vo;

import lombok.Data;

@Data
public class UserVo {

    private String userId;
    private String userName;
    private String nickName;
    private String pwd;
    private String emailAddress;
    private int loginType;

    @Override
    public String toString() {
        return "UserVo{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", loginType=" + loginType +
                '}';
    }
}
