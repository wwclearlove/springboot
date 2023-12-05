package com.glasssix.dubbo.service.impl.impl;

import com.glasssix.dubbo.pojo.MyMessage;
import com.glasssix.dubbo.service.impl.PushService;
import com.glasssix.dubbo.utils.CacheUtil;
import com.glasssix.dubbo.utils.MsgUtil;
import com.glasssix.dubbo.utils.RedisUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
public class PushServiceImpl implements PushService {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public void pushMsgToOne( MyMessage msg){
        Map<String, Channel> userChannelMap = CacheUtil.cacheChannel;
        Channel channel = userChannelMap.get(msg.getToChannelId());
        if (null != channel) {
            channel.writeAndFlush(new TextWebSocketFrame(MsgUtil.obj2Json(msg)));
            return;
        }
        log.info("接收消息的用户不在本服务端，PUSH！");
        redisUtil.push("uav-flight-message", MsgUtil.obj2Json(msg));
    }
}