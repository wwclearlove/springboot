package com.glasssix.dubbo.service;

import java.util.Map;

public interface PaymentInfoService {

    void createPaymentInfo(Map<String, Object> params);

    void createPaymentInfoForAliPay(Map<String, String> params);
}
