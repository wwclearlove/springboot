package com.glasssix.dubbo.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glasssix.dubbo.enumeration.EnumEventMessageType;
import com.glasssix.dubbo.enumeration.EnumRequestMessageType;
import com.glasssix.dubbo.enumeration.EnumResponseMessageType;
import com.glasssix.dubbo.model.AccessTokenVO;
import com.glasssix.dubbo.model.WeixinUserInfoVO;
import com.glasssix.dubbo.model.vo.*;
import com.glasssix.dubbo.utils.HttpUtil;
import com.glasssix.dubbo.utils.MessageUtil;
import com.glasssix.dubbo.utils.WxCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@DubboService
public class WxServiceImpl implements WxService {

    @Override
    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 回复文本消息
            TextMessageVO textMessage = new TextMessageVO();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setMsgType(EnumResponseMessageType.TEXT.getType());

            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候重试！";

            // 事件推送
            if (EnumRequestMessageType.getEnum(msgType) == EnumRequestMessageType.EVENT) {
                // 事件类型
                String eventType = requestMap.get("Event");
                switch (EnumEventMessageType.getEnum(eventType)) {
                    // 订阅
                    case SUBSCRIBE:
                        respContent = "亲，感谢您的关注！";
                        log.info("亲，感谢您的关注！");
                        break;
                    // 取消订阅
                    case UNSUBSCRIBE:
                        log.info("openid为{}的用户取消关注！",fromUserName);
                        // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                        break;
                    // 自定义菜单点击事件
                    case CLICK:
                        // TODO 自定义菜单权没有开放，暂不处理该类消息
                        break;
                    default:
                        break;
                }
            } else {
                respContent = EnumRequestMessageType.getEnum(msgType).getTypeValue();
            }
            textMessage.setContent(respContent);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    @Override
    public String processRequestReturnSameMsg(HttpServletRequest request) {
        String respMessage = null;
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 回复消息
            BaseMessageVO baseMessageVO = new BaseMessageVO();
            baseMessageVO.setToUserName(fromUserName);
            baseMessageVO.setFromUserName(toUserName);
            baseMessageVO.setCreateTime(System.currentTimeMillis());

            switch (EnumRequestMessageType.getEnum(msgType)) {
                // 文本消息
                case TEXT:
                    TextMessageVO textMessageVO = BeanUtil.copyProperties(baseMessageVO, TextMessageVO.class);
                    textMessageVO.setContent(requestMap.get("Content"));
                    respMessage = MessageUtil.textMessageToXml(textMessageVO);
                    break;
                // 图片消息
                case IMAGE:
                    ImageMessageVO imageMessageVO = BeanUtil.copyProperties(baseMessageVO, ImageMessageVO.class);
                    ImageMessageVO.Image image = new ImageMessageVO.Image(requestMap.get("MediaId"));
                    imageMessageVO.setImage(image);
                    respMessage = MessageUtil.imageMessageToXml(imageMessageVO);
                    break;
                // 语音消息
                case VOICE:
                    VoiceMessageVO voiceMessageVO = BeanUtil.copyProperties(baseMessageVO, VoiceMessageVO.class);
                    VoiceMessageVO.Voice voice = new VoiceMessageVO.Voice(requestMap.get("MediaId"));
                    voiceMessageVO.setVoice(voice);
                    respMessage = MessageUtil.voiceMessageToXml(voiceMessageVO);
                    break;
                // TODO 视频消息 - 失败，原因未知！
                case VIDEO:
                    VideoMessageVO videoMessageVO = BeanUtil.copyProperties(baseMessageVO, VideoMessageVO.class);
                    VideoMessageVO.Video video = new VideoMessageVO.Video(requestMap.get("MediaId"), "视频消息", "这是一条视频消息");
                    videoMessageVO.setVideo(video);
                    respMessage = MessageUtil.videoMessageToXml(videoMessageVO);
                    break;
                // 事件推送
                case EVENT:
                    // 事件类型
                    String eventType = requestMap.get("Event");
                    switch (EnumEventMessageType.getEnum(eventType)) {
                        // 订阅
                        case SUBSCRIBE:
                            NewsMessageVO newsMessage = BeanUtil.copyProperties(baseMessageVO, NewsMessageVO.class);
                            NewsMessageVO.Article article = new NewsMessageVO.Article();
                            article.setTitle("感谢您的关注，这是一条图文消息！");
                            article.setDescription("mysql转oracle笔记");
                            article.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/iaUVVC0premhqE0TrtLzM6ABMIKKjnu81hraZvXia52byYMqADCyqXKwbs2wJ6jiadWc7MypLKL4EC5mUzXZKH2Rg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1");
                            article.setUrl("https://mp.weixin.qq.com/s?__biz=Mzg2NzEwMjc3Ng==&mid=2247483813&idx=1&sn=7a18081426d014ddccd203d33011f526&chksm=ce41f8f2f93671e4fc5d93292360fd24da4cf1434415befc37f25be2d40780b4a485653861d8&mpshare=1&scene=1&srcid=&sharer_sharetime=1579140222251&sharer_shareid=936076bf8d5bee83e89fd7e769b5c6db&key=c62bc26d01d4cb91d588b8abdeaca0fbba6d713fbc52e3b4c4a9f0377e231e0fe6b4ce07f287f509e37cefa17a0346475f12d85e21bcdbb8e953d0685018a874fbd80005417e94836ad9b0ff7559b334&ascene=1&uin=MTg4MzA0MzMxNA%3D%3D&devicetype=Windows+7&version=62070158&lang=zh_CN&exportkey=AYq%2FJJZv5hRr7YLluyVInZk%3D&pass_ticket=vCPgwidZSOs1xBfcd5SrzkCdVlApSWF7Xc%2BOzjYf8GlJ9%2BLQco9XYzTHe9yWHqc1");
                            newsMessage.addArticle(article);
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);
                            break;
                        // 取消订阅
                        case UNSUBSCRIBE:
                            // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                            break;
                        // 自定义菜单点击事件
                        case CLICK:
                            // TODO 自定义菜单权没有开放，暂不处理该类消息
                            TextMessageVO textVO = BeanUtil.copyProperties(baseMessageVO, TextMessageVO.class);
                            textVO.setContent("点我干嘛...");
                            respMessage = MessageUtil.textMessageToXml(textVO);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    @Override
    public String customerLogin(Integer type) {
        String code;
        //登录类型拼接
        String redirecturl = WxCommon.REDIRECTURL;
        try {
            redirecturl = URLEncoder.encode(redirecturl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (type == 1) {
            code = getCode(redirecturl);
        } else {
            code = getCodeInfo(redirecturl);
        }
        return code;
    }

    @Override
    public void getOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("进入微信回调接口");
        // 获取微信授权端返回的code
        String code = request.getParameter("code");
        Map openIdMap = getOpenIdInfo(code);
        String openId = (String) openIdMap.get("openid");
        log.info("获取的openId" + openId);

//        log.info("获取的路径" + WxCommon.URL);
//        String indexUrl = WxCommon.URL.replace("OPENID", openId).replace("PHONE", phone);
//        log.info("回调的路径" + indexUrl);
//        response.setHeader("Location", indexUrl);//设置新请求的URL

    }


    /**
     * @author ph
     * //获取微信公众号用户的静默授权的openid
     * scope=snsapi_base 静默
     * scope=snsapi_userinfo  需要用户同意获取用户的信息
     */
    public String getCode(String redirectUrl) {
        String jmpath = WxCommon.JMPATH;
        String url = jmpath.replace("APPID", WxCommon.APPID).replace("REDIRECT_URI", redirectUrl);
        return url;
    }

    /**
     * @author ph
     * //获取微信公众号用户的静默授权的openid
     * scope=snsapi_base 静默
     * scope=snsapi_userinfo  需要用户同意获取用户的信息
     */
    public String getCodeInfo(String redirectUrl) {
        String sdpath = WxCommon.SDPATH;
        String url = sdpath.replace("APPID", WxCommon.APPID).replace("REDIRECT_URI", redirectUrl);
        System.out.println(url);
        return url;
    }

    /**
     * @author ph
     * <p>
     * 获取用户code 的回调方法，会把code信息返回在此方法中
     * 静默授权回调,只获取openId就可以了
     */
    public Map getOpenIdInfo(String code) throws IOException {
        Map map = new HashMap();
        String tokenpath = WxCommon.TOKENPATH;
        log.info("tokenpath:" + tokenpath);
        String url = tokenpath.replace("APPID", WxCommon.APPID).replace("APPSECRET", WxCommon.APPSECRET).replace("CODE", code);
        log.info("url:" + url);
        String request = HttpUtil.getRequest(url);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(request);
        if (null != json) {
            log.info("访问返回的数据" + json);
            try {
                String accessToken = json.get("access_token").toString();
                String refreshToken = json.get("refresh_token").toString();
                String openid = json.get("openid").toString();
                log.info("获取的openId");
                map.put("access_token", accessToken);
                map.put("refresh_token", refreshToken);
                map.put("openid", openid);
                String url2 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
                String request1 = HttpUtil.getRequest(url2);
                //解析相应内容（转换成json对象）
                JSONObject json2 = JSONObject.parseObject(request1);
                map.put("nickname", json2.getString("nickname"));
                map.put("headimgurl", json2.getString("headimgurl"));
                WeixinUserInfoVO  weixinUserInfoVO = JSON.parseObject(request1, WeixinUserInfoVO.class);;
                System.err.println(weixinUserInfoVO.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }


}