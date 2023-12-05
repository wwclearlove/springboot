package com.glasssix.dubbo.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {

    /**
     * 这里是使用注解的方式定位需要拦截的方法
     * 也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...) 如果以注解可以使用 && 链接
     *
     * @Pointcut("execution(* *.getReceiptInfo(..))")
     */
    @Pointcut("execution(* com..*.controller.*.*(..)) ")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void methodBefore(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //打印请求内容
        //获取当前登录人，util可以自己写
//        log.info("用户信息：{}", ObjectUtil.isNull(SecurityUtils.getUser()) ? "用户未登录" : String.format("uid=%s;phone=%s;username=%s", user.getId(), user.getPhone(), user.getUsername()));
//        log.info("请求ip："+request.getRemoteAddr());
        log.info("请求地址：{},请求方式：{}，请求类方法：{}，请求类方法参数：{}",
                request.getRequestURL().toString(), request.getMethod(),
                joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }

    //在方法执行完结后打印返回内容
    @AfterReturning(returning = "o", pointcut = "pointCut()")
    public void methodAfterReturing(Object o) {
        log.info("返回数据:{}", JSONObject.toJSONString(o));
    }

    /**
     * 使用nginx做反向代理，需要用该方法才能取到真实的远程IP
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
