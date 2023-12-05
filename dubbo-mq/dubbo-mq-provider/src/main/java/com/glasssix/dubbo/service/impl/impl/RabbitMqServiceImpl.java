package com.glasssix.dubbo.service.impl.impl;

import com.glasssix.dubbo.constant.MqConstants;
import com.glasssix.dubbo.service.RabbitMqService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public boolean sendMsg(String data, String msg) {
        rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_NAME,
                MqConstants.ROUTING_KEY_NAME, data, new CorrelationData(msg));
        return true;
    }

    @Override
    public boolean sendMsg(String key, String data, String msg) {
        rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_NAME,
                key, data, new CorrelationData(msg));
        return true;
    }
}
