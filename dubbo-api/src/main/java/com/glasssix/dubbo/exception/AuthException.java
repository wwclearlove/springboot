package com.glasssix.dubbo.exception;

import lombok.Data;

/**
 * @program: java-nwpu
 * @description: 没有访问权限
 * @author: wenjiang
 * @create: 2020-05-06 16:30
 **/
@Data
public class AuthException extends RuntimeException {

    private static final String ERROR_MESSAGE = "无访问权限，请联系管理员授权。";
    private static final Integer ERROR_CODE = 403;

    private Integer code;
    private String msg;

    public AuthException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public AuthException() {
        super(ERROR_MESSAGE);
        this.code = ERROR_CODE;
        this.msg = ERROR_MESSAGE;
    }
}
