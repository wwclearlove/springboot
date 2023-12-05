package com.glasssix.dubbo.exception;

import lombok.Data;

/**
 * @program: java-nwpu
 * @description:
 * @author: wenjiang
 * @create: 2020-05-06 16:30
 **/
@Data
public class GlobalException extends RuntimeException {
    private String msg;
    private int code;

    public GlobalException(String msg) {
        super(msg);
        this.code = -1;
        this.msg = msg;
    }

    public GlobalException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
}
