package com.dollar.penguin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DollarPenguinApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DollarPenguinApplication.class, args);
    }
}
