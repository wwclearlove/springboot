package com.glasssix.dubbo;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@DubboComponentScan
@MapperScan("com.glasssix.dubbo.**.mapper")
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
public class WxPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WxPayApplication.class, args);
    }
}
