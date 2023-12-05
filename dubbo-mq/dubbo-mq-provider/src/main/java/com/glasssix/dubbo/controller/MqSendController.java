package com.glasssix.dubbo.controller;

import com.alibaba.fastjson.JSON;
import com.glasssix.dubbo.constant.MqConstants;
import com.glasssix.dubbo.service.MqService;
import com.glasssix.dubbo.websocket.MessagePO;
import com.glasssix.dubbo.websocket.TopicMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqSendController {
    @Autowired
    MqService mqService;
    @GetMapping("/test")
    public void test(){
        System.err.println("test");
    }
    @GetMapping("/send")
    public void send(@RequestParam String topic,@RequestParam String userId){
        MessagePO po= MessagePO.builder().messageId(userId).type("1").messageId("消息测试").build();
        String body = JSON.toJSONString(new TopicMsg(userId, po));
        mqService.sendMsg(topic,body, MqConstants.LOG_MESSAGE_TAG);
    }
}
