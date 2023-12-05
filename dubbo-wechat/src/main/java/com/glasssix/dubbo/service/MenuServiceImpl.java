package com.glasssix.dubbo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.glasssix.dubbo.exception.GlobalException;
import com.glasssix.dubbo.model.WeixinResponseResult;
import com.glasssix.dubbo.model.menu.Menu;
import com.glasssix.dubbo.utils.HttpUtil;
import com.glasssix.dubbo.utils.WxCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p> 菜单 - 服务实现类 </p>
 *
 * @author : wyc
 * @description :
 * @date : 2020/1/16 16:15
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    RedisService<String> stringRedisService;
    public static final String XCXPAERNTACCESSTOKEN = "accessToken:xcx";

    @Override
    public String getAccessToken() {
        String s = stringRedisService.get(XCXPAERNTACCESSTOKEN);
        if (ObjectUtils.isNotEmpty(s)) {
            return s;
        } else {
            String appid = WxCommon.APPID;
            String appSecret = WxCommon.APPSECRET;
            String accessTokenUrl = WxCommon.ACCESS_TOKEN_URL;
            String requestUrl = accessTokenUrl.replace("APPID", appid).replace("APPSECRET", appSecret);
            String request = HttpUtil.getRequest(requestUrl);
            //解析相应内容（转换成json对象）
            JSONObject json = JSONObject.parseObject(request);
            //判断json是否为空
            if (ObjectUtils.isEmpty(json.getString("errcode"))) {
                String accessToken = json.getString("access_token");
                log.info("accessToken:" + accessToken);
                //存在redis中
                stringRedisService.set(XCXPAERNTACCESSTOKEN, accessToken, 3600);
                return accessToken;
            } else {
                log.error(json.getString("errcode"));
                // 获取token失败
                throw new GlobalException("获取token失败");
            }
        }
    }

    @Override
    public Object getMenu( ) {

        Object menu = restTemplate.getForObject(WxCommon.GET_MENU_URL.replace("ACCESS_TOKEN",  getAccessToken()), Object.class);
        return menu;
    }

    @Override
    public WeixinResponseResult deleteMenu( ) {
        WeixinResponseResult result = restTemplate.getForObject(WxCommon.DELETE_MENU_URL.replace("ACCESS_TOKEN", getAccessToken()), WeixinResponseResult.class);
        return result;
    }

    @Override
    public WeixinResponseResult createMenu(Menu menu) {
        // 将菜单对象转换成json字符串
      String token=  getAccessToken();
        String jsonMenu = JSON.toJSONString(menu);
        WeixinResponseResult result = restTemplate.postForObject(WxCommon.CREATE_MENU_URL.replace("ACCESS_TOKEN", token), jsonMenu, WeixinResponseResult.class);
        return result;
    }

}
