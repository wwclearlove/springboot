package com.glasssix.dubbo.service;

public interface RocketMqService {

    boolean sendMsg(String data, String msg);

    boolean sendMsg(String topic, String data, String msg);
}

