package com.glasssix.dubbo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface WxPayService {

    /**
     * 获取商户证书序列号
     *
     * @return
     */
    String getMchSerialNumber();

    /**
     * 获取我想平台证书序列号
     *
     * @return
     */
    String getPlatSerialNumber();

    /**
     * 获取平台证书进行初始化
     *
     * @return
     */
    String getPlatformCertificate();

    /**
     * 微信支付回调
     *
     * @param request
     * @param response
     */
    void payNotify(HttpServletRequest request, HttpServletResponse response);

    Map<String, Object> nativePay(Long productId) throws Exception;

    void processOrder(Map<String, Object> bodyMap) throws GeneralSecurityException;

    void cancelOrder(String orderNo) throws Exception;

    String queryOrder(String orderNo) throws Exception;

    void checkOrderStatus(String orderNo) throws Exception;

    void refund(String orderNo, String reason) throws Exception;

    String queryRefund(String orderNo) throws Exception;

    void checkRefundStatus(String refundNo) throws Exception;

    void processRefund(Map<String, Object> bodyMap) throws Exception;

    String queryBill(String billDate, String type) throws Exception;

    String downloadBill(String billDate, String type) throws Exception;



    /**
     * 退款通知
     * @param request
     * @param response
     */
    void refundsNotify(HttpServletRequest request, HttpServletResponse response);
}
