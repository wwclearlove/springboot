package com.glasssix.dubbo.service.impl;

import com.glasssix.dubbo.pojo.MyMessage;

public interface PushService {
    /**
     * 推送给指定用户
     * @param channelId
     * @param msg
     */
    void pushMsgToOne(MyMessage msg);

}