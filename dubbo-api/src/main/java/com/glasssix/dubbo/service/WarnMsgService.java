package com.glasssix.dubbo.service;


import com.glasssix.dubbo.websocket.MessagePO;

public interface WarnMsgService {

    /**
     * 推送消息
     * @param msg
     */
    void push(MessagePO msg);

    /**
     * 推送消息
     * @param userId 用户id
     * @param msg
     */
    void push(String userId, MessagePO msg);




}
