package com.glasssix.dubbo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: ph
 **/
@Component
public class WxCommon {


    /**
     * 查询菜单接口 - GET请求
     */
    public static final String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    /**
     * 删除菜单接口 - GET请求 （注意，在个性化菜单时，调用此接口会删除默认菜单及全部个性化菜单）
     */
    public static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    /**
     * 创建菜单接口 - POST请求
     */
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";



    //静默授权路径
    public static String JMPATH;
    //手动授权路径
    public static String SDPATH;
    //Access token 地址 获取 openid的时候使用
    public static String TOKENPATH;
    //微信消息模板通知的url
    public static String PUSHMESSAGE_PATH;
    //获取access_token的接口地址
    public static String ACCESS_TOKEN_URL;


    public static String APPID;
    public static String APPSECRET;
    public static String YUMING;

    public static String REDIRECTURL;
    public static String QRCODE;
    @Value("${gzh.qrcode}")
    public void qrcode(String qrcode) {
        WxCommon.QRCODE = qrcode;
    }
    @Value("${gzh.appid}")
    public void appid(String message) {
        WxCommon.APPID = message;
    }

    @Value("${gzh.appsecret}")
    public void appsecret(String message) {
        WxCommon.APPSECRET = message;
    }

    @Value("${gzh.yuming}")
    public void yuming(String message) {
        WxCommon.YUMING = message;
    }



    @Value("${gzh.redirectUrl}")
    public void redirectUrl(String message) {
        WxCommon.REDIRECTURL = message;
    }

    @Value("${gzh.jmpath}")
    public void jmpath(String message) {
        WxCommon.JMPATH = message;
    }

    @Value("${gzh.sdpath}")
    public void sdpath(String message) {
        WxCommon.SDPATH = message;
    }

    @Value("${gzh.tokenpath}")
    public void tokenpath(String message) {
        WxCommon.TOKENPATH = message;
    }

    @Value("${gzh.pushmessagePath}")
    public void pushmessagePath(String message) {
        WxCommon.PUSHMESSAGE_PATH = message;
    }

    @Value("${gzh.accessTokenUrl}")
    public void accessTokenUrl(String message) {
        WxCommon.ACCESS_TOKEN_URL = message;
    }
}

