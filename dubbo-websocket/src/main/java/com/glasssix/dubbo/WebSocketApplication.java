package com.glasssix.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // 排除启动自动加载数据配置，不配置会启动失败
@EnableDiscoveryClient
@DubboComponentScan
public class WebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);
        log.info("WebSocketApplication启动完成!");
    }

}

