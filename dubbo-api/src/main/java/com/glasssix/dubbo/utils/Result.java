package com.glasssix.dubbo.utils;


import com.glasssix.dubbo.constant.CodeMsg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author majun
 */
@Data
@Builder
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final String SUCCESS = "操作成功";
    private String message;//提示信息
    private int code;//0:成功;其他:不成功
    private Integer epcCacheHashCode;
    private T data;//结果数据

    public Result() {
        this.code = 0;
    }


    private Result(CodeMsg cm) {
        if (cm == null) {
            return;
        } else {
            this.code = cm.getCode();
            this.message = cm.getMessage();
        }
    }


    public static Result error(int code, String msg) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(msg);
        return r;
    }
    public static Result error(String msg) {
        Result r = new Result();
        r.setCode(-1);
        r.setMessage(msg);
        return r;
    }
    public static Result error(String msg,Object data) {
        Result r = new Result();
        r.setCode(-1);
        r.setData(data);
        r.setMessage(msg);
        return r;
    }
    public static Result error(int code,String msg,Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setData(data);
        r.setMessage(msg);
        return r;
    }
    public static Result check(boolean result) {
        if (result) {
            return Result.saveSuccess();
        } else {
            return Result.saveFailed();
        }
    }


    public static Result getFailed() {
        Result r = new Result();
        r.setCode(-1);
        r.setMessage("获取失败");
        return r;
    }

    public static Result saveSuccess() {
        Result r = new Result();
        r.setMessage("保存成功");
        return r;
    }

    public static Result failedParam(String filedName) {
        Result r = new Result();
        r.setCode(-1);
        r.setMessage("参数异常:" + filedName);
        return r;
    }
    public static Result ok(Object data,String message) {
        Result r = new Result();
        r.setMessage(message);
        r.setData(data);
        return r;
    }
    public static Result ok(Object data) {
        Result r = new Result();
        r.setMessage(SUCCESS);
        r.setData(data);
        return r;
    }

    public static Result ok() {
        Result r = new Result();
        r.setMessage(SUCCESS);
        return r;
    }

    public static Result saveFailed() {
        Result r = new Result();
        r.setCode(-1);
        r.setMessage("操作失败");
        return r;
    }

    public static Result removeSuccess() {
        Result r = new Result();
        r.setMessage("操作成功");
        return r;
    }

    public static Result removeFailed() {
        Result r = new Result();
        r.setCode(-1);
        r.setMessage("操作失败");
        return r;
    }

    public static Result updateSuccess() {
        Result r = new Result();
        r.setMessage("操作成功");
        return r;
    }

    public static Result updateFailed() {
        Result r = new Result();
        r.setMessage("操作失败");
        r.setCode(-1);
        return r;
    }

    public Result setData(T data) {
        this.message = SUCCESS;
        this.data = data;
        return this;
    }

}
