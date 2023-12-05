package com.glasssix.dubbo.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class CommonWebSocketServer {

    //当前在线用户
    private static final AtomicInteger onlineCount = new AtomicInteger(0);


    // 当前登录用户的id和websocket session的map
    private static ConcurrentHashMap<Session, String> userIdSessionMap = new ConcurrentHashMap<>();



    /**
     * 连接开启时调用
     *
     * @param userId
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        if (userId != null) {
            log.info("websocket 新客户端连入，用户id：" + userId);
            userIdSessionMap.put(session, userId);
            addOnlineCount();
            // 发送消息返回当前用户
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 200);
            jsonObject.put("message", "OK");
            send(userId, JSON.toJSONString(jsonObject));
        } else {
            log.error("websocket连接 缺少参数 id");
            throw new IllegalArgumentException("websocket连接 缺少参数 id");
        }
    }

    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose(Session session) {
        log.info("一个客户端关闭连接");
        subOnlineCount();
        userIdSessionMap.remove(session);
    }

    /**
     * 服务端接收到信息后调用
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户发送过来的消息为：" + message);
    }

    /**
     * 服务端websocket出错时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket出现错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送信息给客户端
     * @param id 用户ID
     * @param message 发送的消息
     */
    public void send(String id, String message) {
        log.info("#### 点对点消息，userId={}", id);
        if(userIdSessionMap.size() > 0) {
            List<Session> sessionList = new ArrayList<>();
            for (Map.Entry<Session, String> entry : userIdSessionMap.entrySet()) {
                if(id.equalsIgnoreCase(entry.getValue())) {
                    sessionList.add(entry.getKey());
                }
            }
            if(sessionList.size() > 0) {
                for (Session session : sessionList) {
                    try {
                        session.getBasicRemote().sendText(message);//发送string
                        log.info("推送用户【{}】消息成功，消息为：【{}】", id , message);
                    } catch (Exception e) {
                        log.info("推送用户【{}】消息失败，消息为：【{}】，原因是：【{}】", id , message, e.getMessage());
                    }
                }
            } else {
                log.error("未找到当前id对应的session， id = {}", id);
            }
        } else {
            log.warn("当前无websocket连接");
        }
    }

    /**
     * 广播消息
     * @param message
     */
    public void broadcast(String message) {
        log.info("#### 广播消息");
        if(userIdSessionMap.size() > 0) {
            for (Map.Entry<Session, String> entry : userIdSessionMap.entrySet()) {
                try {
                    entry.getKey().getBasicRemote().sendText(message);//发送string
                } catch (Exception e) {
                    log.error("websocket 发送【{}】消息出错：{}",entry.getKey(), e.getMessage());
                }
            }
        } else {
            log.warn("当前无websocket连接");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

    /**
     * 自定义消息编码器
     */
    public static class MessageEncoder implements Encoder.Text<JSONObject> {
        @Override
        public void init(javax.websocket.EndpointConfig endpointConfig) {

        }
        @Override
        public void destroy () {
        }

        @Override
        public String encode(JSONObject object) throws EncodeException {
            return object == null ? "" : object.toJSONString();
        }
    }
}