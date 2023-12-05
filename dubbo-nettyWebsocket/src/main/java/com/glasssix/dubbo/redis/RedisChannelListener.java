package com.glasssix.dubbo.redis;

import com.alibaba.fastjson.JSON;
import com.glasssix.dubbo.pojo.MyMessage;
import com.glasssix.dubbo.utils.CacheUtil;
import com.glasssix.dubbo.utils.MsgUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;

@Slf4j
public class RedisChannelListener implements MessageListener {


    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        log.info("sub message :) channel[cleanNoStockCache] !");

        log.info("接收到PUSH消息：{}", message);
        MyMessage msgAgreement = JSON.parseObject(message.getBody(), MyMessage.class);
        String toChannelId = msgAgreement.getToChannelId();
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null == channel) {
            return;
        }
        // 发送消息
        channel.writeAndFlush(new TextWebSocketFrame(MsgUtil.obj2Json(msgAgreement) + "  redis listener"));
    }

}
