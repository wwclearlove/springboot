package com.glasssix.dubbo.config;


import com.glasssix.dubbo.constant.MqConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Data
@Configuration
public class RabbitMqProducerConfigure {
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    // 声明交换机
    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange(MqConstants.EXCHANGE_NAME).durable(true).build();
    }

    // 声明队列
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(MqConstants.QUEUE_NAME).build();
    }

    // 队列与交换机绑定
    @Bean
    public Binding bindQueueExchange(@Qualifier("queue") Queue queue, @Qualifier("exchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MqConstants.ROUTING_KEY_NAME).noargs();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * data：消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            if(ack){
                log.info("{}=======>消息发送成功",msgId);
            }else {
                log.error("{}=======>消息发送失败",msgId);
            }
        });
        /**
         * 消息失败回调，比如router不到queue时回调
         * msg：消息主题
         * repCode：响应码
         * repText：相应描述
         * exchange：交换机
         * routingkey：路由键
         */
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingkey)->{
            log.error("{}=======>消息发送queue时失败",msg.getBody());
        });
        return rabbitTemplate;
    }
}