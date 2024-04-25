package com.lucky.apibackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lucky
 * @date 2024/4/15
 * @description
 */
@SpringBootApplication
@MapperScan("com.lucky.apibackend.mapper")
public class ApiBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiBackendApplication.class, args);
    }
}
