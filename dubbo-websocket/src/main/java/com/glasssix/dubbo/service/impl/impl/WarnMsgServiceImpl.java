package com.glasssix.dubbo.service.impl.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.glasssix.dubbo.service.WarnMsgService;
import com.glasssix.dubbo.websocket.MessagePO;
import com.glasssix.dubbo.websocket.CommonWebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@DubboService
public class WarnMsgServiceImpl implements WarnMsgService {

    @Autowired
    CommonWebSocketServer webSocketHandler;

    @Override
    public void push(MessagePO msg) {
        push("", msg);
    }

    @Override
    public void push(String userId, MessagePO msg) {
        Assert.notNull(msg, "消息对象不能为空！");
        if(StringUtils.isEmpty(userId)) {
            webSocketHandler.broadcast(JSON.toJSONString(msg));
        } else {
            webSocketHandler.send(userId, JSON.toJSONString(msg));
        }
    }
}
