package com.glasssix.dubbo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glasssix.dubbo.domain.PaymentInfo;
import com.glasssix.dubbo.enums.PayType;
import com.glasssix.dubbo.mapper.PaymentInfoMapper;
import com.glasssix.dubbo.service.PaymentInfoService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    /**
     * 记录支付日志：微信支付
     * @param params
     */
    @Override
    public void createPaymentInfo(Map<String, Object> params) {

        log.info("记录支付日志");

        //订单号
        String orderNo = (String)params.get("out_trade_no");
        //业务编号
        String transactionId = (String)params.get("transaction_id");
        //支付类型
        String tradeType = (String)params.get("trade_type");
        //交易状态
        String tradeState = (String)params.get("trade_state");
        //用户实际支付金额
        Map<String, Object> amount = (Map)params.get("amount");
        Integer payerTotal = ((Double) amount.get("payer_total")).intValue();

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.WXPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType(tradeType);
        paymentInfo.setTradeState(tradeState);
        paymentInfo.setPayerTotal(payerTotal);
        paymentInfo.setContent(params.toString());

        baseMapper.insert(paymentInfo);
    }

    /**
     * 记录支付日志：支付宝
     * @param params
     */
    @Override
    public void createPaymentInfoForAliPay(Map<String, String> params) {

        log.info("记录支付日志");

        //获取订单号
        String orderNo = params.get("out_trade_no");
        //业务编号
        String transactionId = params.get("trade_no");
        //交易状态
        String tradeStatus = params.get("trade_status");
        //交易金额
        String totalAmount = params.get("total_amount");
        int totalAmountInt = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();


        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.ALIPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType("电脑网站支付");
        paymentInfo.setTradeState(tradeStatus);
        paymentInfo.setPayerTotal(totalAmountInt);

        Gson gson = new Gson();
        String json = gson.toJson(params, HashMap.class);
        paymentInfo.setContent(json);

        baseMapper.insert(paymentInfo);
    }
}
