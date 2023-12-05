package com.glasssix.dubbo.processor;


import com.alibaba.fastjson.JSONObject;
import com.glasssix.dubbo.constant.MqConstants;
import com.glasssix.dubbo.websocket.TopicMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class RabbitMqListener {
    @RabbitListener(queues = MqConstants.QUEUE_NAME)
//    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "${rabbit.queue}"), exchange = @Exchange(value = "${rabbit.exchange}", type = ExchangeTypes.TOPIC), key = "${rabbit.key}")})
    public void handler(Message message) {
        MessageHeaders headers = message.getHeaders();
        //获取rabbit目前的key
        String tag = (String) headers.get("spring_returned_message_correlation");
        System.err.println(tag);
        if (tag.equals(MqConstants.LOG_MESSAGE_TAG)) {
            TopicMsg topicMsg = JSONObject.parseObject(message.getPayload().toString(), TopicMsg.class);
            log.info("message.body = {}", topicMsg);
        }

    }
}
