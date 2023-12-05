package com.glasssix.dubbo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录信息存储
 */
@Data
public class LoginToken implements Serializable {


    private Long personId;
    private String personName;
    private String name;
    private String phone;
    private Long roleId;
    private String roleName;


}
