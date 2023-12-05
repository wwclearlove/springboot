package com.glasssix.dubbo.config;

import cn.hutool.extra.mail.MailAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MailAccountConfig {

    @Bean
    public MailAccount mailAccount() {
        MailAccount account = new MailAccount();
        // 发送服务器 从第一个链接 获取
        account.setHost("smtp.qiye.aliyun.com");
        // 发送邮件服务的 端口 从第一个链接 获取
//        account.setPort(25);
        // 如果启用 465 端口发送 必须要保证 当前主机 能 telnet smtp.mxhichina.com 465 能够连通，如果能够连通，Java爆错，请详细查看自己账号密码，或者认证密码是否正确
        account.setPort(465);
        // 是否认证
        account.setAuth(true);
        // 发件人（必须正确，否则发送失败）
        account.setFrom("glasssix@glasssix.com");
        // 发送邮箱的账号
        account.setUser("glasssix@glasssix.com");
        // 开启 465 端口 必须设置这个
        account.setStarttlsEnable(true);
        // 发送邮箱账号的密码
        account.setPass("zcPdmStvQ8gtNTAT");
        return account;
    }
}
