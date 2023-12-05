package com.glasssix.dubbo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = {"test"},groupId = "wyc")
    public void onMessage(String msg){
            log.info("消费消息内容：{}",msg);
    }
}
