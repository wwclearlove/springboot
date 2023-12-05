package com.glasssix.dubbo.constants;

import org.springframework.stereotype.Component;

@Component
public class PayCommon {
    //授权类型
    public static final String GRANT_TYPE = "authorization_code";

    //根据code换openId地址
    public static final String CODETOOPENIDURL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=GRANT_TYPE";
    //通过统一下单来获取预付单信息
    public static final String UNIFIEDORDERURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //关闭微信订单的地址
    public static final String CLOSEORDERURL = "https://api.mch.weixin.qq.com/pay/closeorder";
    //退款地址
    public static final String REFUNDURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //静默授权路径
    public static final String JMPATH = "https://open. weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
    //手动授权路径
    public static final String SDPATH = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    //Access token 地址 获取 openid的时候使用
    public static final String TOKENPATH = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    //获取access_token的接口地址
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //查询支付订单url
    public static final String ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
    //查询退款订单url
    public static final String RETURN_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";
    //微信消息模板通知的url
    public static final String PUSHMESSAGE_PATH = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=ACCESS_TOKEN";
}

 