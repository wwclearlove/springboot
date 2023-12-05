package com.glasssix.dubbo.service;

public interface MqService {
    boolean sendMsg(String topicOrKey, String data, String msg);
}

