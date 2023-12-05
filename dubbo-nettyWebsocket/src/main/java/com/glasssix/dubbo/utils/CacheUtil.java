package com.glasssix.dubbo.utils;

import com.glasssix.dubbo.server.WebsocketServer;
import com.glasssix.dubbo.pojo.ServerInfo;
import io.netty.channel.Channel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CacheUtil {

    // 缓存channel
    public static Map<String, Channel> cacheChannel = Collections.synchronizedMap(new HashMap<String, Channel>());
//    private static ConcurrentHashMap<String,Channel> cacheChannel = new ConcurrentHashMap<>();

    // 缓存服务信息
    public static Map<Integer, ServerInfo> serverInfoMap = Collections.synchronizedMap(new HashMap<Integer, ServerInfo>());

    // 缓存 websocket 服务端
    public static Map<Integer, WebsocketServer> serverMap = Collections.synchronizedMap(new HashMap<Integer, WebsocketServer>());

}
