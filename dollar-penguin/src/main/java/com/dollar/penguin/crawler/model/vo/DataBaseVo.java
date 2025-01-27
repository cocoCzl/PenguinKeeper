package com.dollar.penguin.crawler.model.vo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

@Data
public class DataBaseVo {

    @NotNull(message = "用户ID不能为空！", groups = Update.class)
    private int id;
    private String dataBaseName;
    @NotEmpty(message = "数据库URL不能为空！")
    private String url;
    @NotEmpty(message = "密码不能为空！")
    private String pwd;
    @NotEmpty(message = "用户名称不能为空！")
    private String userName;

    public interface Add extends Default {

    }

    public interface Update extends Default {

    }
}
