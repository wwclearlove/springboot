package com.glasssix.dubbo.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.glasssix.dubbo.exception.GlobalException;
import com.glasssix.dubbo.service.MenuServiceImpl;
import com.glasssix.dubbo.service.RedisService;
import com.glasssix.dubbo.service.WxService;
import com.glasssix.dubbo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
@Slf4j
@RestController
@RequestMapping("/auth")
public class WxAuthorizeController {
    @Autowired
    private WxService wxService;
    // TODO 这里的token是微信公众平台上自己所配的！
    private static final String token = "wyctest";
    @Autowired
    RedisService<String> stringRedisService;
    /**
     * 处理微信认证：验证服务器地址的有效性，get提交
     * signature: 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * timestamp 时间戳
     * nonce: 随机数
     * echostr: 随机字符串
     */
    @GetMapping
    public String checkSignature(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("============= 处理微信认证 ===============");
        // 拿到微信的请求参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        // ① 将token、timestamp、nonce三个参数进行字典序排序 b a d c h ==>a b c d h
        String[] strArr = {token, timestamp, nonce};
        // 字典排序
        Arrays.sort(strArr);
        // ② 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuffer sb = new StringBuffer();
        // 字符串拼接
        for (String str : strArr) {
            sb.append(str);
        }
        // 加密
        String sha1Str = SecurityUtil.sha1(sb.toString());
        // ③ 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (sha1Str.equals(signature)) {
            // 如果相等，就是来自微信请求
            // 若确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效
           return echostr;
        }
        return null;
    }
    /**
     * 解析请求消息，post请求
     */
    @PostMapping
    public void msgProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("============= 处理微信认证 ===============");
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 调用核心业务类接收消息、处理消息
//        String respMessage = wxService.processRequest(request);
        String respMessage = wxService.processRequestReturnSameMsg(request);
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(respMessage);
        out.close();
    }

    /**
     * 授权
     *
     * @param type 1 静默授权  2需要用户同意获取用户的信息
     * @return
     */
    @GetMapping("/login")
    public Result customerLogin(Integer type) {
        return Result.ok(wxService.customerLogin(type));
    }

    @GetMapping("/getOpenId")
    public void getOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        wxService.getOpenId(request, response);
    }
    @GetMapping("/getQrCode")
    public Result getQrCode() throws Exception {
        // 获取token开发者
        String accessToken =getAccessToken();
        String getQrCodeUrl = WxCommon.QRCODE.replace("TOKEN", accessToken);
        // 这里生成一个带参数的二维码，参数是scene_str
        String sceneStr = CodeLoginUtil.getRandomString(8);
        String json="{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\"" +", \"action_info\": {\"scene\": {\"scene_str\": \""+sceneStr+"\"}}}";
        String result  = HttpClientUtil.doPostJson(getQrCodeUrl,json);
        JSONObject jsonObject = JSONObject.parseObject(result);
        jsonObject.put("sceneStr",sceneStr);
        return Result.ok(jsonObject);
    }

    public String getAccessToken() {
        String s = stringRedisService.get(MenuServiceImpl.XCXPAERNTACCESSTOKEN);
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
                stringRedisService.set(MenuServiceImpl.XCXPAERNTACCESSTOKEN, accessToken, 3600);
                return accessToken;
            } else {
                log.error(json.getString("errcode"));
                // 获取token失败
                throw new GlobalException("获取token失败");
            }
        }
    }

}
