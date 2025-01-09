package com.dollar.penguin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.dollar.penguin.crawler.mapper","com.dollar.penguin.user.mapper"})
public class MyBatisConfig {

}
