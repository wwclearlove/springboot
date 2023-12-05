package com.glasssix.dubbo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.glasssix.dubbo.server.WebsocketServer;
import com.glasssix.dubbo.pojo.MyMessage;
import com.glasssix.dubbo.pojo.ServerInfo;
import com.glasssix.dubbo.pojo.UserChannelInfo;
import com.glasssix.dubbo.service.impl.PushService;
import com.glasssix.dubbo.utils.CacheUtil;
import com.glasssix.dubbo.utils.RedisUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Inet4Address;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@RestController
@RequestMapping
@Slf4j
public class WebSocketController {


    @Value("${netty.port}")
    private int port;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PushService service;
    @Autowired
    private WebsocketServer websocketServer;

    private static ExecutorService executorService = Executors.newCachedThreadPool();


    /**
     * 开启 netty
     *
     * @return
     */
    @RequestMapping("/openNettyServer")
    public String openNettyServer() {

        try {

            log.info("启动Netty服务，获取可用端口：{}", port);
            Future<Channel> future = executorService.submit(websocketServer);
            Channel channel = future.get();
            if (null == channel) {
                throw new RuntimeException("netty server open error channel is null");
            }
            while (!channel.isActive()) {
                log.info("启动Netty服务，循环等待启动...");
                Thread.sleep(500);
            }
            // 放入 缓存
            CacheUtil.serverInfoMap.put(port, new ServerInfo( Inet4Address.getLocalHost().getHostAddress(), port, new Date()));
            CacheUtil.serverMap.put(port, websocketServer);
            //获取nacos服务
            NamingService namingService = NamingFactory.createNamingService("localhost:8848");
            //将服务注册到注册中心
            namingService.registerInstance("nettyservice","127.0.0.1", Integer.valueOf(port));
            String serverStatus = namingService.getServerStatus();
            log.info("nettyservice"+serverStatus);
            log.info("nettyservice"+"注册到nacos成功");
            log.info("启动Netty服务，完成：{}", channel.localAddress());
            return "ok";
        } catch (Exception e) {
            log.error("启动Netty服务失败", e);
            return "error";
        }
    }

    /**
     * 关闭 netty
     *
     * @return
     */
    @RequestMapping("/closeNettyServer")
    public String closeNettyServer() {
        try {
            log.info("关闭Netty服务开始，端口：{}", port);
            WebsocketServer websocketServer = CacheUtil.serverMap.get(port);
            if (null == websocketServer) {
                CacheUtil.serverMap.remove(port);
                return "ok";
            }
            websocketServer.destroy();
            CacheUtil.serverMap.remove(port);
            CacheUtil.serverInfoMap.remove(port);
            NamingService namingService = NamingFactory.createNamingService("localhost:8848");
            namingService.deregisterInstance("nettyservice","127.0.0.1", Integer.valueOf(port));
            String serverStatus = namingService.getServerStatus();
            log.info("nettyservice"+"注销到nacos成功");
            log.info("关闭Netty服务完成，端口：{}", port);
            return "ok";
        } catch (Exception e) {
            log.error("关闭Netty服务失败，端口：{}", port, e);
            return "error";
        }
    }

    /**
     *  查 服务端 列表
     * @return
     */
    @RequestMapping("/queryNettyServerList")
    public Collection<ServerInfo> queryNettyServerList() {
        try {
            Collection<ServerInfo> serverInfos = CacheUtil.serverInfoMap.values();
            log.info("查询服务端列表。{}", JSON.toJSONString(serverInfos));
            return serverInfos;
        } catch (Exception e) {
            log.info("查询服务端列表失败。", e);
            return null;
        }
    }

    /**
     *  从 redis 查 用户管道
     * @return
     */
    @RequestMapping("/queryUserChannelInfoList")
    public List<UserChannelInfo> queryUserChannelInfoList() {
        try {
            log.info("查询用户列表信息开始");
            List<UserChannelInfo> userChannelInfoList = redisUtil.popList();
            log.info("查询用户列表信息完成。list：{}", JSON.toJSONString(userChannelInfoList));
            return userChannelInfoList;
        } catch (Exception e) {
            log.error("查询用户列表信息失败", e);
            return null;
        }
    }
    @RequestMapping("/send")
    public String send(@RequestBody  MyMessage msg) {
        try {
            service.pushMsgToOne(msg);
            return "ok";
        } catch (Exception e) {
            return "error";
        }
    }
}
