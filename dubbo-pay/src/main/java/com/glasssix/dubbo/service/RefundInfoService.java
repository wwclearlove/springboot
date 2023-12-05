package com.glasssix.dubbo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glasssix.dubbo.domain.RefundInfo;

import java.util.List;
import java.util.Map;

public interface RefundInfoService extends IService<RefundInfo> {

    RefundInfo createRefundByOrderNo(String orderNo, String reason);

    void updateRefund(Map<String, Object> bodyMap);

    List<RefundInfo> getNoRefundOrderByDuration(int minutes);

    RefundInfo createRefundByOrderNoForAliPay(String orderNo, String reason);

    void updateRefundForAliPay(String refundNo, String content, String refundStatus);
}
