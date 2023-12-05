package com.glasssix.dubbo.processor;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.glasssix.dubbo.constant.MqConstants;
import com.glasssix.dubbo.websocket.TopicMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {

    /**
     * 默认msg里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
     * 不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     *
     * @param msgList
     * @param consumeConcurrentlyContext
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt messageExt : msgList) {
            try {
//                log.info("收到消息:{},     {},  次数:{}", messageExt.getTags(), messageExt.getMsgId(), messageExt.getReconsumeTimes());
                if (messageExt.getReconsumeTimes() <= 5) {
                    String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
                    if (messageExt.getTags().equals(MqConstants.LOG_MESSAGE_TAG)) {
                        log.info("");
                        TopicMsg topicMsg = JSONObject.parseObject( msg, TopicMsg.class);
                        log.info("message.body = {}", topicMsg);
                        //保存操作日志

                    } else {
                        //无效的tag
                    }
                }
            } catch (JSONException  | IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


}
