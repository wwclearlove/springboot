package com.glasssix.dubbo.service.impl;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.glasssix.dubbo.config.WxPayV3Bean;
import com.glasssix.dubbo.constants.WxStatusConstants;
import com.glasssix.dubbo.domain.OrderInfo;
import com.glasssix.dubbo.domain.RefundInfo;
import com.glasssix.dubbo.enums.OrderStatus;
import com.glasssix.dubbo.enums.PayType;
import com.glasssix.dubbo.enums.wxpay.WxRefundStatus;
import com.glasssix.dubbo.enums.wxpay.WxTradeState;
import com.glasssix.dubbo.exception.GlobalException;
import com.glasssix.dubbo.service.*;
import com.glasssix.dubbo.utils.UrlUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.AesUtil;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.enums.v3.OtherApiEnum;
import com.ijpay.wxpay.model.v3.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {


    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private PaymentInfoService paymentInfoService;

    @Resource
    private RefundInfoService refundsInfoService;
    @Autowired
    RedisLock redisLock;
    @Resource
    WxPayV3Bean wxPayV3Bean;
    private String mchSerialNo;
    private String platSerialNo;

    @Override
    public String getMchSerialNumber() {
        if (StrUtil.isEmpty(mchSerialNo)) {
            // 获取证书序列号
            X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(wxPayV3Bean.getCertPath()));
            if (null != certificate) {
                mchSerialNo = certificate.getSerialNumber().toString(16).toUpperCase();
//            boolean isValid = PayKit.checkCertificateIsValid(certificate, wxPayV3Bean.getMchId(), 2);
//            log.info("证书是否可用 {} 证书有效期为 {}", isValid, DateUtil.format(certificate.getNotAfter(), DatePattern.NORM_DATETIME_PATTERN));
            }
//            System.out.println("输出证书信息:\n" + certificate.toString());
//            // 输出关键信息，截取部分并进行标记
//            System.out.println("证书序列号:" + certificate.getSerialNumber().toString(16));
//            System.out.println("版本号:" + certificate.getVersion());
//            System.out.println("签发者：" + certificate.getIssuerDN());
            log.info("serialNo:{},证书有效起始日期 {},有效终止日期 {}", mchSerialNo, DateUtil.format(certificate.getNotBefore(), DatePattern.NORM_DATETIME_PATTERN), DateUtil.format(certificate.getNotAfter(), DatePattern.NORM_DATETIME_PATTERN));
//            System.out.println("主体名：" + certificate.getSubjectDN());
//            System.out.println("签名算法：" + certificate.getSigAlgName());
//            System.out.println("签名：" + certificate.getSignature().toString());
        }
        log.info("mchSerialNo:{}", mchSerialNo);
        return mchSerialNo;
    }

    @Override
    public String getPlatSerialNumber() {
        if (StrUtil.isEmpty(platSerialNo)) {
            // 获取平台证书序列号
            X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(wxPayV3Bean.getPlatformCertPath()));
            platSerialNo = certificate.getSerialNumber().toString(16).toUpperCase();
        }
        log.info("platSerialNo:{}", platSerialNo);
        return platSerialNo;
    }

    @Override
    public String getPlatformCertificate() {
        IJPayHttpResponse response = null;
        try {
            response = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    OtherApiEnum.GET_CERTIFICATES.toString(),
                    wxPayV3Bean.getMchId(),
                    getMchSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    ""
            );
            String serialNumber = response.getHeader("Wechatpay-Serial");
            String body = response.getBody();
            int status = response.getStatus();

            log.info("serialNumber: {}", serialNumber);
            log.info("status: {}", status);
            log.info("body: {}", body);
            if (status == WxStatusConstants.OK) {
                JSONObject jsonObject = JSONUtil.parseObj(body);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                // 默认认为只有一个平台证书
                JSONObject encryptObject = dataArray.getJSONObject(0);
                JSONObject encryptCertificate = encryptObject.getJSONObject("encrypt_certificate");
                String associatedData = encryptCertificate.getStr("associated_data");
                String cipherText = encryptCertificate.getStr("ciphertext");
                String nonce = encryptCertificate.getStr("nonce");
                String serialNo = encryptObject.getStr("serial_no");
                final String platSerialNo = savePlatformCert(associatedData, nonce, cipherText, wxPayV3Bean.getPlatformCertPath());
                log.info("平台证书序列号: {} serialNo: {}", platSerialNo, serialNo);
            }
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature:{}", verifySignature);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>(12);
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");
            log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            String result = HttpKit.readData(request);
            log.info("支付通知密文 {}", result);
            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    wxPayV3Bean.getApiKey3(), wxPayV3Bean.getPlatformCertPath());
            log.info("支付通知明文 {}", plainText);
            Gson gson = new Gson();
            Map<String, Object> bodyMap = gson.fromJson(plainText, HashMap.class);
            processOrder(bodyMap);
            if (StrUtil.isNotEmpty(plainText)) {
                response.setStatus(200);
                map.put("code", "SUCCESS");
                map.put("message", "SUCCESS");
            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }
            response.setHeader("Content-type", ContentType.JSON.toString());
            response.getOutputStream().write(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String savePlatformCert(String associatedData, String nonce, String cipherText, String certPath) {
        try {
            AesUtil aesUtil = new AesUtil(wxPayV3Bean.getApiKey3().getBytes(StandardCharsets.UTF_8));
            // 平台证书密文解密
            // encrypt_certificate 中的  associated_data nonce  ciphertext
            String publicKey = aesUtil.decryptToString(
                    associatedData.getBytes(StandardCharsets.UTF_8),
                    nonce.getBytes(StandardCharsets.UTF_8),
                    cipherText
            );
            // 保存证书
            FileWriter writer = new FileWriter(certPath);
            writer.write(publicKey);
            // 获取平台证书序列号
            X509Certificate certificate = PayKit.getCertificate(new ByteArrayInputStream(publicKey.getBytes()));
            return certificate.getSerialNumber().toString(16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 创建订单，调用Native支付接口
     *
     * @param productId
     * @return code_url 和 订单号
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> nativePay(Long productId) throws Exception {
        log.info("生成订单");
        //生成订单
        OrderInfo orderInfo = orderInfoService.createOrderByProductId(productId, PayType.WXPAY.getType());
        String codeUrl = orderInfo.getCodeUrl();
        if (orderInfo != null && !StringUtils.isEmpty(codeUrl)) {
            log.info("订单已存在，二维码已保存");
            //返回二维码
            Map<String, Object> map = new HashMap<>();
            map.put("codeUrl", codeUrl);
            map.put("orderNo", orderInfo.getOrderNo());
            return map;
        }
        log.info("调用统一下单API");
        //调用统一下单API
        String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 5);
        UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                .setAppid(wxPayV3Bean.getAppId())
                .setMchid(wxPayV3Bean.getMchId())
                .setDescription("测试支付")
                .setOut_trade_no(orderInfo.getOrderNo())
                .setTime_expire(timeExpire)
                .setAttach("微信系开发脚手架")
                .setNotify_url(wxPayV3Bean.getDomain().concat("/api/wx-pay/payNotify"))
                .setAmount(new Amount().setTotal(1));

        log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
        IJPayHttpResponse response = WxPayApi.v3(
                RequestMethodEnum.POST,
                WxDomainEnum.CHINA.toString(),
                BasePayApiEnum.NATIVE_PAY.toString(),
                wxPayV3Bean.getMchId(),
                getMchSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(),
                JSONUtil.toJsonStr(unifiedOrderModel)
        );
        log.info("统一下单响应 {}", response);
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
        log.info("verifySignature: {}", verifySignature);
        if (response.getStatus() == WxStatusConstants.OK) {
            //二维码
            String responseBody = response.getBody();
            JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            codeUrl = jsonObject.getStr("code_url");
            //保存二维码
            String orderNo = orderInfo.getOrderNo();
            orderInfoService.saveCodeUrl(orderNo, codeUrl);
            //返回二维码
            Map<String, Object> map = new HashMap<>();
            map.put("codeUrl", codeUrl);
            map.put("orderNo", orderInfo.getOrderNo());
            return map;
        } else {
            log.error("Native下单失败,响应码 = " + response.getStatus() + ",返回结果 = " + response.getBody());
            throw new GlobalException("request failed");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processOrder(Map<String, Object> plainTextMap) {
        log.info("处理订单");
        String orderNo = (String) plainTextMap.get("out_trade_no");
        /*在对业务数据进行状态检查和处理之前，
        要采用数据锁进行并发控制，
        以避免函数重入造成的数据混乱*/
        //尝试获取锁：
        // 成功获取则立即返回true，获取失败则立即返回false。不必一直等待锁的释放
        if (redisLock.tryLock(orderNo, orderNo)) {
            try {
                //处理重复的通知
                //接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的。
                String orderStatus = orderInfoService.getOrderStatus(orderNo);
                if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
                    return;
                }
                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);

                //记录支付日志
                paymentInfoService.createPaymentInfo(plainTextMap);
            } finally {
                //要主动释放锁
                redisLock.releaseLock(orderNo, orderNo);
            }
        }
    }

    /**
     * 用户取消订单
     *
     * @param orderNo
     */
    @Override
    public void cancelOrder(String orderNo) throws Exception {
        //调用微信支付的关单接口
        this.closeOrder(orderNo);
        //更新商户端的订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);
    }

    @Override
    public String queryOrder(String orderNo) throws Exception {
        log.info("查单接口调用 ===> {}", orderNo);
        String url = String.format(BasePayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO.toString(), orderNo);

        Map<String, String> params = new HashMap<>(12);
        params.put("mchid", wxPayV3Bean.getMchId());
        IJPayHttpResponse result = WxPayApi.v3(
                RequestMethodEnum.GET,
                WxDomainEnum.CHINA.toString(),
                url,
                wxPayV3Bean.getMchId(),
                getMchSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(),
                params
        );
        log.info("result:{}", result);
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
        log.info("verifySignature: {}", verifySignature);
        String body = result.getBody();
        int status = result.getStatus();
        if (status == WxStatusConstants.OK) {
            log.info("成功200");
        } else {
            log.info("Native关单失败,响应码 = " + status);
            throw new IOException("request failed");
        }
        return body;
    }

    /**
     * 根据订单号查询微信支付查单接口，核实订单状态
     * 如果订单已支付，则更新商户端订单状态，并记录支付日志
     * 如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
     *
     * @param orderNo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkOrderStatus(String orderNo) throws Exception {
        log.warn("根据订单号核实订单状态 ===> {}", orderNo);
        //调用微信支付查单接口
        String result = this.queryOrder(orderNo);
        Gson gson = new Gson();
        Map<String, Object> resultMap = gson.fromJson(result, HashMap.class);
        //获取微信支付端的订单状态
        String tradeState = (String) resultMap.get("trade_state");
        //判断订单状态
        if (WxTradeState.SUCCESS.getType().equals(tradeState)) {
            log.warn("核实订单已支付 ===> {}", orderNo);
            //如果确认订单已支付则更新本地订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
            //记录支付日志
            paymentInfoService.createPaymentInfo(resultMap);
        }

        if (WxTradeState.NOTPAY.getType().equals(tradeState)) {
            log.warn("核实订单未支付 ===> {}", orderNo);
            //如果订单未支付，则调用关单接口
            this.closeOrder(orderNo);

            //更新本地订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
        }
    }

    /**
     * 退款
     *
     * @param orderNo
     * @param reason
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(String orderNo, String reason) throws Exception {

        log.info("创建退款单记录");
        //根据订单编号创建退款单
        RefundInfo refundsInfo = refundsInfoService.createRefundByOrderNo(orderNo, reason);
//
        log.info("调用退款API");
        List<RefundGoodsDetail> list = Lists.newArrayList();
        RefundGoodsDetail refundGoodsDetail = new RefundGoodsDetail()
                .setMerchant_goods_id("123")
                .setGoods_name("退款商品")
                .setUnit_price(refundsInfo.getRefund())
                .setRefund_amount(refundsInfo.getTotalFee())
                .setRefund_quantity(1);
        list.add(refundGoodsDetail);

        RefundModel refundModel = new RefundModel()
                .setOut_refund_no(refundsInfo.getRefundNo())//退款单编号
                .setOut_trade_no(orderNo)//订单编号
                .setReason("reason")
                .setNotify_url(wxPayV3Bean.getDomain().concat("/api/wx-pay/refunds/notify"))
                .setAmount(new RefundAmount().setRefund(refundsInfo.getRefund()).setTotal(refundsInfo.getTotalFee()).setCurrency("CNY"))
                .setGoods_detail(list);
        log.info("退款参数 {}", JSONUtil.toJsonStr(refundModel));
        IJPayHttpResponse response = WxPayApi.v3(
                RequestMethodEnum.POST,
                WxDomainEnum.CHINA.toString(),
                BasePayApiEnum.REFUND.toString(),
                wxPayV3Bean.getMchId(),
                getMchSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(),
                JSONUtil.toJsonStr(refundModel)
        );
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
        log.info("verifySignature: {}", verifySignature);
        log.info("退款响应 {}", response);
        if (response.getStatus() == WxStatusConstants.OK) {
            log.info("成功, 退款返回结果");
        } else {
            throw new GlobalException("退款异常, 响应码 = " + response.getStatus() + ", 退款返回结果 = " + response);
        }
        //更新订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_PROCESSING);
        //更新退款单
        String body = response.getBody();
        Gson gson=new Gson();
        Map<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
        refundsInfoService.updateRefund(bodyMap);

    }


    /**
     * 查询退款接口调用
     *
     * @param refundNo
     * @return
     */
    @Override
    public String queryRefund(String refundNo) throws Exception {

        log.info("查询退款接口调用 ===> {}", refundNo);
        String url = String.format(BasePayApiEnum.REFUND_QUERY_BY_OUT_REFUND_NO.toString(), refundNo);
        Map<String, String> params = new HashMap<>(12);
        params.put("mchid", wxPayV3Bean.getMchId());
        IJPayHttpResponse result = WxPayApi.v3(
                RequestMethodEnum.GET,
                WxDomainEnum.CHINA.toString(),
                url,
                wxPayV3Bean.getMchId(),
                getMchSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(), ""
        );
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
        log.info("verifySignature: {}", verifySignature);
        log.info("result:{}", result);
        String body = result.getBody();
        int status = result.getStatus();
        if (status == WxStatusConstants.OK) {
            log.info("成功200");
        } else {
            log.info("Native关单失败,响应码 = " + status);
            throw new IOException("request failed");
        }
        return body;
    }

    /**
     * 根据退款单号核实退款单状态
     *
     * @param refundNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkRefundStatus(String refundNo) throws Exception {

        log.warn("根据退款单号核实退款单状态 ===> {}", refundNo);

        //调用查询退款单接口
        String result = this.queryRefund(refundNo);

        //组装json请求体字符串
        Gson gson = new Gson();
        Map<String, Object> resultMap = gson.fromJson(result, HashMap.class);

        //获取微信支付端退款状态
        String status = (String) resultMap.get("status");

        String orderNo = (String) resultMap.get("out_trade_no");

        if (WxRefundStatus.SUCCESS.getType().equals(status)) {

            log.warn("核实订单已退款成功 ===> {}", refundNo);

            //如果确认退款成功，则更新订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

            //更新退款单
            refundsInfoService.updateRefund(resultMap);
        }

        if (WxRefundStatus.ABNORMAL.getType().equals(status)) {

            log.warn("核实订单退款异常  ===> {}", refundNo);

            //如果确认退款成功，则更新订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_ABNORMAL);

            //更新退款单
            refundsInfoService.updateRefund(resultMap);
        }
    }

    /**
     * 处理退款单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processRefund(Map<String, Object> bodyMap) throws Exception {

        log.info("退款单");

        String orderNo = (String) bodyMap.get("out_trade_no");

        if (redisLock.tryLock(orderNo, orderNo)) {
            try {

                String orderStatus = orderInfoService.getOrderStatus(orderNo);
                if (!OrderStatus.REFUND_PROCESSING.getType().equals(orderStatus)) {
                    return;
                }

                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

                //更新退款单
                refundsInfoService.updateRefund(bodyMap);

            } finally {
                //要主动释放锁
                redisLock.releaseLock(orderNo, orderNo);
            }
        }
    }

    /**
     * 申请账单
     *
     * @param billDate
     * @param type
     * @return
     * @throws Exception
     */
    @Override
    public String queryBill(String billDate, String type) throws Exception {
        log.warn("申请账单接口调用 {}", billDate);

        String url = "";
        if ("tradebill".equals(type)) {
            url = BasePayApiEnum.TRADE_BILL.toString();
        } else if ("fundflowbill".equals(type)) {
            url = BasePayApiEnum.FUND_FLOW_BILL.toString();
        } else {
            throw new RuntimeException("不支持的账单类型");
        }
        Map<String, String> params = new HashMap<>(12);
        params.put("bill_date", billDate);
        IJPayHttpResponse result = WxPayApi.v3(
                RequestMethodEnum.GET,
                WxDomainEnum.CHINA.toString(),
                url,
                wxPayV3Bean.getMchId(),
                getMchSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(),
                params
        );
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
        log.info("verifySignature: {}", verifySignature);
        log.info("result:{}", result);
        int statusCode = result.getStatus();
        String body = result.getBody();
        if (statusCode == WxStatusConstants.OK) {
            log.info("成功, 申请账单返回结果 = " + body);
        } else {
            throw new RuntimeException("申请账单异常, 响应码 = " + statusCode + ", 申请账单返回结果 = " + body);
        }
        JSONObject jsonObject = JSONUtil.parseObj(body);
        String downloadUrl = (String) jsonObject.get("download_url");
        return downloadUrl;
    }

    /**
     * 下载账单
     *
     * @param billDate
     * @param type
     * @return
     * @throws Exception
     */
    @Override
    public String downloadBill(String billDate, String type) throws Exception {
        log.warn("下载账单接口调用 {}, {}", billDate, type);

        //获取账单url地址
        String downloadUrl = this.queryBill(billDate, type);
        String token = UrlUtil.getUrlparameter(downloadUrl, "token");
        Map<String, String> params = new HashMap<>(12);
        params.put("token", token);
        IJPayHttpResponse result = WxPayApi.v3(
                RequestMethodEnum.GET,
                WxDomainEnum.CHINA.toString(),
                BasePayApiEnum.BILL_DOWNLOAD.toString(),
                wxPayV3Bean.getMchId(),
                getMchSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(),
                params
        );
        log.info("result:{}", result);
        return result.getBody();
    }



    @Override
    public void refundsNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("退款通知执行");
        Map<String, String> map = new HashMap<>(12);
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");
            log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            String result = HttpKit.readData(request);
            log.info("退款支付通知密文 {}", result);
            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    wxPayV3Bean.getApiKey3(), wxPayV3Bean.getPlatformCertPath());
            log.info("退款支付通知明文 {}", plainText);
            Gson gson = new Gson();
            Map<String, Object> bodyMap = gson.fromJson(plainText, HashMap.class);
            processRefund(bodyMap);
            if (StrUtil.isNotEmpty(plainText)) {
                response.setStatus(200);
                map.put("code", "SUCCESS");
                map.put("message", "SUCCESS");
            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }
            response.setHeader("Content-type", ContentType.JSON.toString());
            response.getOutputStream().write(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关单接口的调用
     *
     * @param orderNo
     */
    private void closeOrder(String orderNo) throws Exception {
        log.info("关单接口的调用，订单号 ===> {}", orderNo);
        String url = String.format(BasePayApiEnum.CLOSE_ORDER_BY_OUT_TRADE_NO.toString(), orderNo);
        Map<String, String> params = new HashMap<>(12);
        params.put("mchid", wxPayV3Bean.getMchId());

        IJPayHttpResponse result = WxPayApi.v3(
                RequestMethodEnum.POST,
                WxDomainEnum.CHINA.toString(),
                url,
                wxPayV3Bean.getMchId(),
                getMchSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(),
                JSONUtil.toJsonStr(params)
        );
        log.info("result:{}", result);
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
        log.info("verifySignature: {}", verifySignature);

        String body = result.getBody();
        int status = result.getStatus();
        if (status == WxStatusConstants.OK) {
            log.info("成功200");
        }else if(status == WxStatusConstants.OKNORESULT){
            log.info("成功204");
        }
        else {
            log.info("Native关单失败,响应码 = " + status);
            throw new IOException("request failed");
        }

    }


}
