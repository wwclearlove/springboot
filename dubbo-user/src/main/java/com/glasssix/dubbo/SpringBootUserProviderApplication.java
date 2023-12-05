package com.glasssix.dubbo;


import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@MapperScan("com.glasssix.dubbo.mapper")
@SpringBootApplication
//@DubboComponentScan
public class SpringBootUserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootUserProviderApplication.class,args);
    }
}
