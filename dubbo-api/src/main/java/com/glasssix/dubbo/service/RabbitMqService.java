package com.glasssix.dubbo.service;

public interface RabbitMqService {

    boolean sendMsg(String data, String msg);

    boolean sendMsg(String key, String data, String msg);
}

