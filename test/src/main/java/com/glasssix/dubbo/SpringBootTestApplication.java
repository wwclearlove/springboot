package com.glasssix.dubbo;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.glasssix.dubbo.mapper")
@SpringBootApplication
public class SpringBootTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApplication.class, args);
    }
}
