package com.glasssix.dubbo.annotation;

import java.lang.annotation.*;

/**
 * @program: java-nwpu
 * @description: token权限校验
 * @author: wenjiang
 * @create: 2020-05-09 11:32
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorityAnnotation {
    String name() default "token";
}