package com.glasssix.dubbo.utils;

import com.alibaba.fastjson.JSON;
import com.glasssix.dubbo.pojo.MyMessage;

public class MsgUtil {

    public static MyMessage buildMsg(String channelId, String content) {
        return new MyMessage(channelId, content);
    }

    public static MyMessage json2Obj(String objJsonStr) {
        return JSON.parseObject(objJsonStr, MyMessage.class);
    }

    public static String obj2Json(MyMessage msgAgreement) {
        return JSON.toJSONString(msgAgreement);
    }
}