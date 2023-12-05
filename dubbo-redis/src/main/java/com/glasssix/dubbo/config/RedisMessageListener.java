package com.glasssix.dubbo.config;//package com.glasssix.dubbo.config;
//
//import com.glasssix.dubbo.constant.RedisConstants;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//
//@Configuration
//public class RedisMessageListener {
//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter)
//    {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, new PatternTopic(RedisConstants.GLASS_TOPIC_MSG));
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(RedisReceiver receiver)
//    {
//        // 消息监听适配器
//        return new MessageListenerAdapter(receiver, "onMessage");
//    }
//
//}
