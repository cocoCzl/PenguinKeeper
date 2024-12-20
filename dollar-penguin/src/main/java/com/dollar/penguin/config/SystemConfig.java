package com.dollar.penguin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    private ThreadConfig threadConfig;

    @Data
    public static class ThreadConfig{
        private int threadCorePoolSize;
        private int threadMaxPoolSize;
        private int queueCapacity;
        private int keepAliveSeconds;
    }
}
