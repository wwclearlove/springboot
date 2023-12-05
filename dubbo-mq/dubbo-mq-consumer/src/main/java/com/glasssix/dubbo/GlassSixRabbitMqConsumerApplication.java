package com.glasssix.dubbo;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
@DubboComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.glasssix.dubbo"})
public class GlassSixRabbitMqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlassSixRabbitMqConsumerApplication.class, args);
    }

}
