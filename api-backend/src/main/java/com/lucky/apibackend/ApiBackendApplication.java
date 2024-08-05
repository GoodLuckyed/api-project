package com.lucky.apibackend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lucky
 * @date 2024/4/15
 * @description
 */
@SpringBootApplication
@MapperScan("com.lucky.apibackend.mapper")
@EnableDubbo
@EnableScheduling
public class ApiBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiBackendApplication.class, args);
    }
}
