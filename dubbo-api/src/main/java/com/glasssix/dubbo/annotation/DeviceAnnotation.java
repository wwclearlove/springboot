package com.glasssix.dubbo.annotation;

import java.lang.annotation.*;

/**
 * @program: g6
 * @description: 签名校验
 * @author: wenjiang
 * @create: 2020-08-28 11:27
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeviceAnnotation {

    /**
     * 设备ID
     *
     * @return
     */
    String appId() default "X-G6-Key";

    /**
     * 时间戳
     *
     * @return
     */
    String timestamp() default "X-G6-Timestamp";

    /**
     * 签名
     *
     * @return
     */
    String sign() default "X-G6-Sign";


    /**
     * 秘钥
     *
     * @return
     */
    String secret() default "108484C23DF22F5A6395CB6D339B8DFB";
}

 