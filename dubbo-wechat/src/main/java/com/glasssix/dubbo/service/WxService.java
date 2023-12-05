package com.glasssix.dubbo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface WxService {
    /**
     * 消息处理
     *
     * @param request:
     * @return: java.lang.String
     */
    String processRequest(HttpServletRequest request);

    /**
     * 原样返回消息内容
     *
     * @param request:
     * @return: java.lang.String
     */
    String processRequestReturnSameMsg(HttpServletRequest request);
    /**
     * 微信授权返回Url
     *
     * @param type
     * @return
     */
    String customerLogin(Integer type);

    /**
     * 微信授权回调
     */
    void getOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
