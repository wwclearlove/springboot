package com.glasssix.dubbo.config;//package com.glasssix.dubbo.config;
//
//import com.alibaba.cloud.commons.lang.StringUtils;
//import com.alibaba.fastjson.JSONObject;
//import com.glasssix.dubbo.constant.RedisConstants;
//
//import com.glasssix.dubbo.com.glasssix.dubbo.service.WarnMsgService;
//import com.glasssix.dubbo.websocket.MessagePO;
//import com.glasssix.dubbo.websocket.TopicMsg;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class RedisReceiver implements MessageListener {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @DubboReference
//    WarnMsgService warnMsgService;
//
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        RedisSerializer stringRedisSerializer = redisTemplate.getKeySerializer();
//        String patternStr = (String) stringRedisSerializer.deserialize(pattern);
//        String channel = (String)stringRedisSerializer.deserialize(message.getChannel());
//        String body = (String)stringRedisSerializer.deserialize(message.getBody());
//        log.info("event = {}, message.channel = {},  message.body = {}", patternStr, channel, body);
//        if (RedisConstants.GLASS_TOPIC_MSG.equals(channel)) {
////            String parse = (String) JSON.parse(body);
//            TopicMsg topicMsg = JSONObject.parseObject( body, TopicMsg.class);
//            String userId = topicMsg.getUserId();
//            MessagePO msg = topicMsg.getMsg();
//            log.debug("com.glasssix.dubbo.receive from topic=[{}] , userId=[{}], msg=[{}]", RedisConstants.GLASS_TOPIC_MSG, userId, msg);
//            // 发送消息 id 为空就是群发消息
//            if (StringUtils.isEmpty(userId)) {
//                warnMsgService.push(msg);
//            } else {
//                warnMsgService.push(userId, msg);
//            }
//        }
//    }
//
//
//
//}
