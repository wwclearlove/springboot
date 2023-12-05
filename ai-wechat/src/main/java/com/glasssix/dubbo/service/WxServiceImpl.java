package com.glasssix.dubbo.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.glasssix.dubbo.exception.GlobalException;
import com.glasssix.dubbo.utils.GsonUtil;
import com.glasssix.dubbo.utils.HttpUtil;
import com.glasssix.dubbo.utils.Result;
import com.glasssix.dubbo.utils.WxCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@DubboService
public class WxServiceImpl implements WxService {
    @Autowired
    RedisService<String> stringRedisService;
    public static final String OPENID = "AI:OPENID:";

    public boolean isUrl(String urls) {
        if (ObjectUtils.isEmpty(urls)) {
            return false;
        }
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?";

        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(urls.trim());
        boolean result = mat.matches();
        return result;
    }

    @Override
    public String generateQrCode(String redirectUrl) {
        if (!isUrl(redirectUrl)) {
            throw new GlobalException("参数异常");
        }
        String requestUrl = null;
        try {
            requestUrl = WxCommon.QRCODE.replace("APPID", WxCommon.APPID).replace("REDIRECT_URI", URLEncoder.encode(redirectUrl, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new GlobalException("回调url编码异常");
        }
        return requestUrl;
    }

    @Override
    public Result callback(String code, String state) {
        String appid = WxCommon.APPID;
        String appSecret = WxCommon.APP_SECRET;
        String accessTokenUrl = WxCommon.ACCESS_TOKEN;
        accessTokenUrl = accessTokenUrl.replace("APPID", appid).replace("SECRET", appSecret).replace("CODE", code);
        String request = HttpUtil.getRequest(accessTokenUrl);
        Map<String, Object> mapAccessToken = GsonUtil.toMaps(request);


        String openid = (String) mapAccessToken.get("openid");
        if (ObjectUtils.isEmpty(openid)) {
            throw new GlobalException("微信请求异常");
        }
        //用于获取用户信息
        String accessToken = (String) mapAccessToken.get("access_token");
        //TODO 判断openId是否绑定
        Boolean bindingFlag = false;
        if (bindingFlag) {
            //已绑定、和登录一样 返回用户一些信息、token
            return Result.ok();
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        stringRedisService.set(OPENID + uuid, openid, 600);
        //未绑定，与前端约定一个code 如101
        return Result.error(101, "用户未绑定", uuid);


    }

    @Override
    public Result getUserInfo(String openid, String accessToken) {
        String userinfoUrl = WxCommon.USERINFO;
        userinfoUrl = userinfoUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
        String userInfo = HttpUtil.getRequest(userinfoUrl);
        //获取返回userinfo字符串扫描人信息
        Map<String, Object> map = GsonUtil.toMaps(userInfo);
        return Result.ok(map);
    }
}