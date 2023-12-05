package com.glasssix.dubbo.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: rocketmq-consumer
 * @description:
 * @author: wenjiang
 * @create: 2021-02-20 15:25
 **/
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq.consumer")
public class MQConsumerConfigure {

    private String groupName;
    private String namesrvAddr;
    private String topics;
    // 消费者线程数据量
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;
    private Integer consumeMessageBatchMaxSize;

    @Autowired
    private MessageListenerConcurrently messageListenerConcurrently;



//    @Autowired
//    MessageListenerOrderly messageListenerOrderly;

    /**
     * mq 消费者配置
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.consumer", value = "isOnOff", havingValue = "on")
    public DefaultMQPushConsumer defaultConsumer() {
        log.info("defaultConsumer 正在创建---------------------------------------");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        log.info("消费数：{}", consumeMessageBatchMaxSize);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        consumer.registerMessageListener(messageListenerConcurrently);
        System.err.println(consumer.getPullBatchSize());
        consumer.setPullBatchSize(500);
        /**
         * 设置consumer第一次启动是从队列头部开始还是队列尾部开始
         * 如果不是第一次启动，那么按照上次消费的位置继续消费
         */
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        consumer.setMessageModel(MessageModel.CLUSTERING);
        try {
            // 设置该消费者订阅的主题和tag，如果订阅该主题下的所有tag，则使用*,
            String[] topicArr = topics.split(";");
            for (String tag : topicArr) {
                String[] tagArr = tag.split("~");
                System.err.println(tagArr[0] + "  " + tagArr[1]);
                consumer.subscribe(tagArr[0], tagArr[1]);
            }
            consumer.start();
            log.info("consumer 创建成功 groupName={}, topics={}, namesrvAddr={}", groupName, topics, namesrvAddr);
        } catch (MQClientException e) {
            log.error("consumer 创建失败!");
        }
        return consumer;
    }
}