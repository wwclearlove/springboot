package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.service.WxService;
import com.glasssix.dubbo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class WechatController {
    @Autowired
    WxService wxService;

    @GetMapping("/login")
    public Result getWxCode(String redirectUrl) {
        return Result.ok(wxService.generateQrCode(redirectUrl));
    }

    @GetMapping("/callback")
    public Result callback(String code, String state) {
        return wxService.callback(code, state);
    }

    /**
     * 测试接口，根据openid获取用户信息
     *
     * @param openid
     * @return
     */
    @GetMapping("/userInfo")
    public Result userInfo(String openid, String accessToken) {
        return wxService.getUserInfo(openid, accessToken);
    }
}
