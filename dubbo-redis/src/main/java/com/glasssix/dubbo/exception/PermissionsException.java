package com.glasssix.dubbo.exception;

import lombok.Data;

/**
 * @program: java-nwpu
 * @description: 登录验证失败
 * @author: wenjiang
 * @create: 2020-05-06 16:30
 **/
@Data
public class PermissionsException extends RuntimeException {

    private static final String ERROR_MESSAGE = "登录失效,请重新登录";
    private static final Integer ERROR_CODE = 401;

    private Integer code;
    private String msg;

    public PermissionsException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public PermissionsException() {
        super(ERROR_MESSAGE);
        this.code = ERROR_CODE;
        this.msg = ERROR_MESSAGE;
    }
}
