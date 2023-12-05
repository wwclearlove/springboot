package com.glasssix.dubbo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: ph
 **/
@Component
public class WxCommon {


    //获取access_token的接口地址
    public static String ACCESS_TOKEN;


    public static String APPID;
    public static String APP_SECRET;

    public static String QRCODE;
    public static String USERINFO;

    @Value("${wx.open.qrcode}")
    public void qrcode(String qrcode) {
        WxCommon.QRCODE = qrcode;
    }

    @Value("${wx.open.appId}")
    public void appid(String appId) {
        WxCommon.APPID = appId;
    }

    @Value("${wx.open.appSecret}")
    public void appSecret(String appSecret) {
        WxCommon.APP_SECRET = appSecret;
    }


    @Value("${wx.open.accessToken}")
    public void accessTokenUrl(String accessToken) {
        WxCommon.ACCESS_TOKEN = accessToken;
    }

    @Value("${wx.open.userinfo}")
    public void userinfo(String userinfo) {
        WxCommon.USERINFO = userinfo;
    }
}

