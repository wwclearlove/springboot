package com.glasssix.dubbo;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.glasssix.dubbo.mapper")
@SpringBootApplication
@EnableScheduling
public class SpringBootPowerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootPowerApplication.class, args);
    }
}
