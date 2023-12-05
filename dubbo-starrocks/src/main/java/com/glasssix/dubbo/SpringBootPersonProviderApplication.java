package com.glasssix.dubbo;



import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@DubboComponentScan
@MapperScan("com.glasssix.**.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class SpringBootPersonProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootPersonProviderApplication.class, args);
    }
}
