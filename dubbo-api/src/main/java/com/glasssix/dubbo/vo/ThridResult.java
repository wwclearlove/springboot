package com.glasssix.dubbo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: rabbit
 * @description:
 * @author: wenjiang
 * @create: 2021-08-14 11:27
 **/
@Data
public class ThridResult implements Serializable {

    public static final Integer SUCCESSS = 0;

    private Integer status;
    private String errorMsg;
    private Object result;
}

 