package com.glasssix.dubbo.constant;

import lombok.Data;

/**
 * @program: java-nwpu
 * @description:
 * @author: wenjiang
 * @create: 2020-05-06 16:29
 **/
@Data
public class CodeMsg {
    //通用异常 4001XX
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    //自定义状态码
    private int code;
    //消息信息
    private String message;

    //模块1 4002XX
    //模块2 4003XX
    //模块3 4004XX
    private CodeMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.message, args);
        return new CodeMsg(code, message);
    }
}

 