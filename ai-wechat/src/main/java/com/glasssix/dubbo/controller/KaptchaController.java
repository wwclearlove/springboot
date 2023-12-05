package com.glasssix.dubbo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.glasssix.dubbo.Test;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1")
public class KaptchaController {
    @Autowired
    MailAccount mailAccount;
    @Autowired
    private TemplateEngine templateEngine;
    @GetMapping("enum")
    public void enumTest(@RequestBody Test test) {

        System.out.println(test.getStateType().getName());
    }
    @GetMapping("test")
    public void test() {
        Context context = new Context();
        context.setVariable("name", "测试用户");
        context.setVariable("url", "https://www.daixiala.com/verify.php?token=ukSoAsWiVNGjsU49vD74gto");
        String mail = templateEngine.process("mail", context);
        MailUtil.send(mailAccount, CollUtil.newArrayList("yongchuanwang@glasssix.com"), "测试标题", mail, true, new File("E:\\GlassSixGitDM\\SpringBoot-dubbo\\ai-wechat\\src\\main\\resources\\static\\index.html"));
    }

    @GetMapping("getKaptcha")
    public void getKaptcha(HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();

        // 保存验证码信息
        String uuid = UUID.randomUUID().toString();
//        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
//        try {
//            specCaptcha.setFont(Captcha.FONT_2);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        String verCode = specCaptcha.text().toLowerCase();\
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);  // 几位数运算，默认是两位
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        captcha.text();  // 获取运算的结果：5
        String string = captcha.text();
        System.out.println(string);
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为30分钟
//        redisUtil.setEx(key, verCode, 30, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        //将验证码信息塞入redis中
        //code是验证码答案
//        redisTemplate.opsForValue().set(verifyKey, code, 2, TimeUnit.MINUTES);
        response.setHeader("Cache-Control", "no-store");
        // no-cache指示请求或响应消息不能缓存
        response.setHeader("Pragma", "no-cache");
        /* expires是response的一个属性,它可以设置页面在浏览器的缓存里保存的时间 ,超过设定的时        间后就过期 。过期后再次
         * 浏览该页面就需要重新请求服务器发送页面数据，如果在规定的时间内再次访问次页面 就不需从服务器传送 直接从缓存中读取。
         * */
        response.setHeader("Expires", "0");
        // servlet接受request请求后接受图片形式的响应
        response.setContentType(MediaType.IMAGE_JPEG.getType());
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }

    }

}
