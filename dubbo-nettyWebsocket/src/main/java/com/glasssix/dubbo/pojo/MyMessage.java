package com.glasssix.dubbo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMessage {

    //发送给某人，某人channelId
    private String toChannelId;

    //消息内容
    private String content;
}
