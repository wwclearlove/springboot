package com.glasssix.dubbo.service.impl.impl;

import com.glasssix.dubbo.constant.MqConstants;
import com.glasssix.dubbo.service.RocketMqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: rabbit
 * @description:
 * @author: wenjiang
 * @create: 2021-08-10 17:49
 **/
@Slf4j
@Service
public class RocketMqServiceImpl implements RocketMqService {

    @Autowired
    DefaultMQProducer defaultMQProducer;

    @Override
    public boolean sendMsg(String data, String tags) {
        try {
            Message msg = new Message(MqConstants.ROCKETMQ_TOPIC, tags, data.getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult send = defaultMQProducer.send(msg);
            if (!send.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("MQ发送失败:  {}", tags);
            }
            return send.getSendStatus().equals(SendStatus.SEND_OK);
        } catch (Exception e) {
            log.info("MQ发送失败:  {}", tags);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendMsg(String topic, String data, String tags) {
        try {
            Message msg = new Message(topic, tags, data.getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult send = defaultMQProducer.send(msg);
            if (!send.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("MQ发送失败:  {}", tags);
            }
            log.info("send{}", send);
            return send.getSendStatus().equals(SendStatus.SEND_OK);
        } catch (Exception e) {
            log.info("MQ发送失败:  {}", tags);
            e.printStackTrace();
        }
        return false;
    }
}

