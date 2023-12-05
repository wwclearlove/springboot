package com.glasssix.dubbo.service.impl.impl;

import com.glasssix.dubbo.service.MqService;
import com.glasssix.dubbo.service.RabbitMqService;
import com.glasssix.dubbo.service.RocketMqService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@DubboService
public class MqServiceImpl implements MqService {
    @Value("${start-rabbitmq}")
    private Integer startRabbitMq;
    @Autowired
    RabbitMqService rabbitMqService;
    @Autowired
    RocketMqService rocketMqService;
    @Override
    public boolean sendMsg(String topicOrKey, String data, String msg) {
        if(startRabbitMq==0){
            rabbitMqService.sendMsg(topicOrKey,data,msg);
        }else if(startRabbitMq==1){
            rocketMqService.sendMsg(topicOrKey,data,msg);
        }
        return true;
    }
}
