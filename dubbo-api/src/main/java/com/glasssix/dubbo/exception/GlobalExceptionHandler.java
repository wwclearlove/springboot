package com.glasssix.dubbo.exception;


import com.glasssix.dubbo.utils.Result;
import org.apache.dubbo.remoting.ExecutionException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.RpcException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;


/**
 * @program: java-nwpu
 * @description:
 * @author: wenjiang
 * @create: 2020-05-06 16:30
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 登录信息异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(PermissionsException.class)
    public Result permissionsException(RuntimeException ex) {
        return resultFormat(401, ex);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity noHandlerFoundException(NoHandlerFoundException ex) {
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(15, "调用服务失败"));
    }

    /**
     * 运行时异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException ex) {
        ex.printStackTrace();
        return resultFormat(1, ex);
    }

    /**
     * 空指针异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public Result nullPointerExceptionHandler(NullPointerException ex) {
        ex.printStackTrace();

        return resultFormat(2, ex);
    }

    /**
     * 类型转换异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public Result classCastExceptionHandler(ClassCastException ex) {
        ex.printStackTrace();
        return resultFormat(3, ex);
    }

    /**
     * IO异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    public Result iOExceptionHandler(IOException ex) {
        ex.printStackTrace();
        return resultFormat(4, ex);
    }

    /**
     * 未知方法异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public Result noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        ex.printStackTrace();
        return resultFormat(5, ex);
    }

    /**
     * 数组越界异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Result indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        ex.printStackTrace();
        return resultFormat(6, ex);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result requestNotReadable(HttpMessageNotReadableException ex) {
        ex.printStackTrace();
        return resultFormat(7, ex);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    public Result requestTypeMismatch(TypeMismatchException ex) {
        ex.printStackTrace();
        return resultFormat(8, ex);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result requestMissingServletRequest(MissingServletRequestParameterException ex) {
        ex.printStackTrace();
        return resultFormat(9, ex);
    }

    /**
     * 405错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result request405(HttpRequestMethodNotSupportedException ex) {
        ex.printStackTrace();
        return resultFormat(10, ex);
    }

    /**
     * 406错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public Result request406(HttpMediaTypeNotAcceptableException ex) {
        ex.printStackTrace();
        return resultFormat(11, ex);
    }

    /**
     * 500错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public Result server500(RuntimeException ex) {
        ex.printStackTrace();
        return resultFormat(12, ex);
    }


    /**
     * 除数不能为0
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({ArithmeticException.class})
    public Result arithmeticException(ArithmeticException ex) {
        ex.printStackTrace();
        return resultFormat(13, ex);
    }

    /**
     * 除数不能为0
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public Result illegalArgumentException(ArithmeticException ex) {
        ex.printStackTrace();
        return resultFormat(15, ex);
    }

    /**
     * 其他错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({Exception.class})
    public Result exception(Exception ex) {
        ex.printStackTrace();
        return resultFormat(14, ex);
    }

    @ExceptionHandler({GlobalException.class})
    public Result globalException(GlobalException ex) {
        ex.printStackTrace();
        return resultFormat(15, ex);
    }

    private Result resultFormat(Integer code, Exception ex) {
        ex.printStackTrace();
        return Result.error(code, ex.getMessage());
    }

    @ExceptionHandler({RpcException.class, TimeoutException.class, ExecutionException.class})
    public ResponseEntity rpcException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(15, "调用服务失败"));
    }

    @ExceptionHandler({BadSqlGrammarException.class, MyBatisSystemException.class})
    public Result sqlException(Exception ex) {
        ex.printStackTrace();
        return Result.error(16, "数据库异常");
    }

}
