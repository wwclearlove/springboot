package com.glasssix.dubbo.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.glasssix.dubbo.config.WxPayV3Bean;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.AesUtil;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.*;
import com.ijpay.wxpay.model.RefundQueryModel;
import com.ijpay.wxpay.model.v3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.*;


@RestController
@RequestMapping("/v3")
public class WxPayV3Controller {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final static int OK = 200;

    @Resource
    WxPayV3Bean wxPayV3Bean;

    String serialNo;
    String platSerialNo;


    @RequestMapping("/getSerialNumber")
    public String serialNumber() {
        return getSerialNumber();
    }

    @RequestMapping("/getPlatSerialNumber")
    public String platSerialNumber() {
        return getPlatSerialNumber();
    }

    private String getSerialNumber() {
        if (StrUtil.isEmpty(serialNo)) {
            // 获取证书序列号
            X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(wxPayV3Bean.getCertPath()));
            serialNo = certificate.getSerialNumber().toString(16).toUpperCase();

//            System.err.println("输出证书信息:\n" + certificate.toString());
//            // 输出关键信息，截取部分并进行标记
//            System.err.println("证书序列号:" + certificate.getSerialNumber().toString(16));
//            System.err.println("版本号:" + certificate.getVersion());
//            System.err.println("签发者：" + certificate.getIssuerDN());
//            System.err.println("有效起始日期：" + certificate.getNotBefore());
//            System.err.println("有效终止日期：" + certificate.getNotAfter());
//            System.err.println("主体名：" + certificate.getSubjectDN());
//            System.err.println("签名算法：" + certificate.getSigAlgName());
//            System.err.println("签名：" + certificate.getSignature().toString());
        }
        System.err.println("serialNo:" + serialNo);
        return serialNo;
    }

    private String getPlatSerialNumber() {
        if (StrUtil.isEmpty(platSerialNo)) {
            // 获取平台证书序列号
            X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(wxPayV3Bean.getPlatformCertPath()));
            platSerialNo = certificate.getSerialNumber().toString(16).toUpperCase();
            System.err.println("platSerialNo:" + platSerialNo);

        }
        return platSerialNo;
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




    @RequestMapping("/get")
    public String v3Get() {
        // 获取平台证书列表
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    OtherApiEnum.GET_CERTIFICATES.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    ""
            );

            String timestamp = response.getHeader("Wechatpay-Timestamp");
            String nonceStr = response.getHeader("Wechatpay-Nonce");
            String serialNumber = response.getHeader("Wechatpay-Serial");
            String signature = response.getHeader("Wechatpay-Signature");

            String body = response.getBody();
            int status = response.getStatus();

            log.info("serialNumber: {}", serialNumber);
            log.info("status: {}", status);
            log.info("body: {}", body);
            int isOk = 200;
            if (status == isOk) {
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
            System.out.println("verifySignature:" + verifySignature);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/nativePay")
    public String nativePay() {
        try {
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(wxPayV3Bean.getAppId())
                    .setMchid(wxPayV3Bean.getMchId())
                    .setDescription("IJPay 让支付触手可及")
                    .setOut_trade_no(PayKit.generateStr())
                    .setTime_expire(timeExpire)
                    .setAttach("微信系开发脚手架 https://gitee.com/javen205/TNWX")
                    .setNotify_url(wxPayV3Bean.getDomain().concat("/v3/payNotify"))
                    .setAmount(new Amount().setTotal(1));

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.NATIVE_PAY.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(unifiedOrderModel)
            );
            log.info("统一下单响应 {}", response);
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/jsApiPay")
    public String jsApiPay(@RequestParam(value = "openId", required = false, defaultValue = "o-_-itxuXeGW3O1cxJ7FXNmq8Wf8") String openId) {
        try {
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(wxPayV3Bean.getAppId())
                    .setMchid(wxPayV3Bean.getMchId())
                    .setDescription("IJPay 让支付触手可及")
                    .setOut_trade_no(PayKit.generateStr())
                    .setTime_expire(timeExpire)
                    .setAttach("微信系开发脚手架 https://gitee.com/javen205/TNWX")
                    .setNotify_url(wxPayV3Bean.getDomain().concat("/v3/payNotify"))
                    .setAmount(new Amount().setTotal(1))
                    .setPayer(new Payer().setOpenid(openId));

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            IJPayHttpResponse response = WxPayApi.v3(
                  RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.JS_API_PAY.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(unifiedOrderModel)
            );
            log.info("统一下单响应 {}", response);

            if (response.getStatus() == OK) {
                // 根据证书序列号查询对应的证书来验证签名结果
                boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
                log.info("verifySignature: {}", verifySignature);
                if (verifySignature) {
                    String body = response.getBody();
                    JSONObject jsonObject = JSONUtil.parseObj(body);
                    String prepayId = jsonObject.getStr("prepay_id");
                    Map<String, String> map = WxPayKit.jsApiCreateSign(wxPayV3Bean.getAppId(), prepayId, wxPayV3Bean.getKeyPath());
                    log.info("唤起支付参数:{}", map);
                    return JSONUtil.toJsonStr(map);
                }
            }
            return JSONUtil.toJsonStr(response);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @RequestMapping("/put")
    public String put() {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("url", "https://gitee.com/javen205/IJPay");

            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.PUT,
                    WxDomainEnum.CHINA.toString(),
                    ComplaintsApiEnum.COMPLAINTS_NOTIFICATION.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(params)
            );
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            log.info("响应 {}", response);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @RequestMapping("/getParams")
    public String payScoreUserServiceState() {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("service_id", "500001");
            params.put("appid", "wxd678efh567hg6787");
            params.put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

            IJPayHttpResponse result = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    PayScoreApiEnum.PAY_SCORE_SERVICE_ORDER.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    params
            );
            System.out.println(result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/delete")
    public String v3Delete() {
        // 创建/查询/更新/删除投诉通知回调
        try {
            HashMap<String, String> hashMap = new HashMap<>(12);
            hashMap.put("url", "https://qq.com");
            IJPayHttpResponse result = WxPayApi.v3(
                  RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    ComplaintsApiEnum.COMPLAINTS_NOTIFICATION.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(hashMap)
            );
            System.out.println(result);

            result = WxPayApi.v3(
                   RequestMethodEnum.DELETE,
                    WxDomainEnum.CHINA.toString(),
                    ComplaintsApiEnum.COMPLAINTS_NOTIFICATION.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    ""
            );
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
            System.out.println("verifySignature:" + verifySignature);
            // 如果返回的为 204 表示删除成功
            System.out.println(result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/upload")
    public String v3Upload() {
        // v3 接口上传文件
        try {
            String filePath = "/Users/Javen/Documents/pic/cat.png";

            File file = FileUtil.newFile(filePath);
            String sha256 = SecureUtil.sha256(file);

            HashMap<Object, Object> map = new HashMap<>();
            map.put("filename", file.getName());
            map.put("sha256", sha256);
            String body = JSONUtil.toJsonStr(map);

            System.out.println(body);

            IJPayHttpResponse result = WxPayApi.v3(
                    WxDomainEnum.CHINA.toString(),
                    OtherApiEnum.MERCHANT_UPLOAD_MEDIA.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    body,
                    file
            );
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
            System.out.println("verifySignature:" + verifySignature);
            System.out.println(result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/post")
    public String payGiftActivity() {
        // 支付有礼-终止活动
        try {
            String urlSuffix = String.format(PayGiftActivityApiEnum.PAY_GIFT_ACTIVITY_TERMINATE.toString(), "10028001");
            System.out.println(urlSuffix);
            IJPayHttpResponse result = WxPayApi.v3(
                  RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    urlSuffix,
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    ""
            );
            System.out.println(result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/sensitive")
    public String sensitive() {
        // 带有敏感信息接口
        try {
            String body = "处理请求参数";

            IJPayHttpResponse result = WxPayApi.v3(
                  RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    Apply4SubApiEnum.APPLY_4_SUB.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    getPlatSerialNumber(),
                    wxPayV3Bean.getKeyPath(),
                    body
            );
            System.out.println(result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/cipher")
    public String cipher() {
        try {
            // 敏感信息加密
            X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(wxPayV3Bean.getPlatformCertPath()));
            String encrypt = PayKit.rsaEncryptOAEP("test", certificate);
            System.out.println(encrypt);
            PrivateKey privateKey = PayKit.getPrivateKey(wxPayV3Bean.getKeyPath());
            String decrypt = PayKit.rsaDecryptOAEP(encrypt, privateKey);
            System.out.println(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    /**
     * 申请交易账单
     *
     * @param billDate 2020-06-14 当天账单后一天出，不然会出现「账单日期格式不正确」错误
     * @return 交易账单下载地址
     */
    @RequestMapping("/tradeBill")
    public String tradeBill(@RequestParam(value = "billDate", required = false) String billDate) {
        try {
            if (StrUtil.isEmpty(billDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -1);
                billDate = DateUtil.format(calendar.getTime(), "YYYY-MM-dd");
            }
            Map<String, String> params = new HashMap<>(12);
            params.put("bill_date", billDate);
            params.put("bill_type", "ALL");
            params.put("tar_type", "GZIP");

            IJPayHttpResponse result = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.TRADE_BILL.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    params
            );
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            log.info("result:{}", result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 申请资金账单
     *
     * @param billDate 2020-06-14 当天账单后一天出，不然会出现「账单日期格式不正确」错误
     * @return 资金账单下载地址
     */
    @RequestMapping("/fundFlowBill")
    public String fundFlowBill(@RequestParam(value = "billDate", required = false) String billDate) {
        try {
            if (StrUtil.isEmpty(billDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -1);
                billDate = DateUtil.format(calendar.getTime(), "YYYY-MM-dd");
            }
            Map<String, String> params = new HashMap<>(12);
            params.put("bill_date", billDate);
            params.put("account_type", "BASIC");

            IJPayHttpResponse result = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.FUND_FLOW_BILL.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    params
            );
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(result, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            log.info("result:{}", result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/billDownload")
    public String billDownload(@RequestParam(value = "token") String token,
                               @RequestParam(value = "tarType", required = false) String tarType) {
        try {

            Map<String, String> params = new HashMap<>(12);
            params.put("token", token);
            if (StrUtil.isNotEmpty(tarType)) {
                params.put("tartype", tarType);
            }

            IJPayHttpResponse result = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.BILL_DOWNLOAD.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    params
            );
            log.info("result:{}", result);
            return JSONUtil.toJsonStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping("/refund")
   
    public String refund(@RequestParam(required = false) String transactionId,
                         @RequestParam(required = false) String outTradeNo) {
        try {
            String outRefundNo = PayKit.generateStr();
            log.info("商户退款单号: {}", outRefundNo);

            List<RefundGoodsDetail> list = new ArrayList<>();
            RefundGoodsDetail refundGoodsDetail = new RefundGoodsDetail()
                    .setMerchant_goods_id("123")
                    .setGoods_name("IJPay 测试")
                    .setUnit_price(1)
                    .setRefund_amount(1)
                    .setRefund_quantity(1);
            list.add(refundGoodsDetail);

            RefundModel refundModel = new RefundModel()
                    .setOut_refund_no(outRefundNo)
                    .setReason("IJPay 测试退款")
                    .setNotify_url(wxPayV3Bean.getDomain().concat("/v3/refundNotify"))
                    .setAmount(new RefundAmount().setRefund(1).setTotal(1).setCurrency("CNY"))
                    .setGoods_detail(list);

            if (StrUtil.isNotEmpty(transactionId)) {
                refundModel.setTransaction_id(transactionId);
            }
            if (StrUtil.isNotEmpty(outTradeNo)) {
                refundModel.setOut_trade_no(outTradeNo);
            }
            log.info("退款参数 {}", JSONUtil.toJsonStr(refundModel));
            IJPayHttpResponse response = WxPayApi.v3(
                  RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.REFUND.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(refundModel)
            );
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            log.info("退款响应 {}", response);

            if (verifySignature) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    @RequestMapping(value = "/payNotify", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
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

//    /**
//     * v3 h5下单
//     *
//     * @param request
//     * @throws Exception
//     */
//    @RequestMapping(value = "/wapPayH5", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
//    public Result wapPayH5(HttpServletRequest request, WXOrder order) throws Exception {
//        try {
//            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
//            H5Info h5Info = new H5Info()
//                    .setType("Wap");//场景类型示例值：iOS, Android, Wap
//            SceneInfo sceneInfo = new SceneInfo()
//                    .setPayer_client_ip(CommonUtil.getIpAddress(request))//调用微信支付API的机器IP，支持IPv4和IPv6两种格式的IP地址。
//                    .setH5_info(h5Info);
//            BigDecimal multiply = order.getAmount().multiply(new BigDecimal("100"));
//            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
//                    .setAppid(wxPayV3Bean.getAppId())//公众号ID
//                    .setMchid(wxPayV3Bean.getMchId())//直连商户号
//                    .setDescription(order.getAreaName())//商品描述
//                    .setOut_trade_no(order.getOrderId())//商户订单号
//                    .setTime_expire(timeExpire)//订单失效时间
//                    .setAttach(order.getAreaName())//附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
//                    .setNotify_url(wxPayV3Bean.getDomain().concat("/v3/payNotifyH5"))//通知地址
//                    .setAmount(new Amount().setTotal(multiply.intValue()).setCurrency("CNY"))//订单总金额，单位为分。
//                    .setScene_info(sceneInfo);//支付场景描述
//
//            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
//            IJPayHttpResponse response = WxPayApi.v3(
//                  RequestMethodEnum.POST,
//                    WxDomainEnum.CHINA.toString(),
//                    BasePayApiEnum.H5_PAY.toString(),
//                    wxPayV3Bean.getMchId(),
//                    getSerialNumber(),
//                    null,
//                    wxPayV3Bean.getKeyPath(),
//                    JSONUtil.toJsonStr(unifiedOrderModel)
//            );
//            log.info("统一下单响应 {}", response);
//            if (response.getStatus() == OK) {
//                // 根据证书序列号查询对应的证书来验证签名结果
//                boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
//                log.info("verifySignature: {}", verifySignature);
//                if (verifySignature) {
//                    String body = response.getBody();
//                    JSONObject jsonObject = JSONUtil.parseObj(body);
//                    String prepayId = jsonObject.getStr("prepay_id");
//                    Map<String, String> map = WxPayKit.jsApiCreateSign(wxPayV3Bean.getAppId(), prepayId, wxPayV3Bean.getKeyPath());
//                    log.info("唤起支付参数:{}", map);
//                    return Result.ok(JSONUtil.toJsonStr(map));
//                }
//            }
//            return Result.ok(JSONUtil.toJsonStr(response));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Result.error("h5统一下单失败");
//    }

    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotifyH5", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    public String payNotifyH5(HttpServletRequest request) throws Exception {
        String xmlMsg = HttpKit.readData(request);
        log.info("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");

        // 注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态

                // 更新订单信息

                // 发送通知等

                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }

//    /**
//     * 微信退款
//     */
//    @RequestMapping(value = "/refundH5", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
//   
//    public String refundH5(WXOrder order) {
//        try {
//            String outRefundNo = PayKit.generateStr();
//            log.info("商户退款单号: {}", outRefundNo);
//            BigDecimal multiply = order.getAmount().multiply(new BigDecimal("100"));
//            List<RefundGoodsDetail> list = new ArrayList<>();
//            RefundGoodsDetail refundGoodsDetail = new RefundGoodsDetail()
//                    .setMerchant_goods_id(order.getId().toString())
//                    .setGoods_name(order.getAreaName())
//                    .setUnit_price(multiply.intValue())
//                    .setRefund_amount(multiply.intValue())
//                    .setRefund_quantity(1);
//            list.add(refundGoodsDetail);
//
//            RefundModel refundModel = new RefundModel()
//                    .setOut_refund_no(outRefundNo)
//                    .setReason(order.getAreaName())
//                    .setNotify_url(wxPayV3Bean.getDomain().concat("/v3/refundNotifyH5"))
//                    .setAmount(new RefundAmount().setRefund(1).setTotal(1).setCurrency("CNY"))
//                    .setGoods_detail(list);
//
//
//            if (StrUtil.isNotEmpty(order.getOrderId())) {
//                refundModel.setOut_trade_no(order.getOrderId());
//            }
//            log.info("退款参数 {}", JSONUtil.toJsonStr(refundModel));
//            IJPayHttpResponse response = WxPayApi.v3(
//                  RequestMethodEnum.POST,
//                    WxDomainEnum.CHINA.toString(),
//                    BasePayApiEnum.DOMESTIC_REFUNDS.toString(),
//                    wxPayV3Bean.getMchId(),
//                    getSerialNumber(),
//                    null,
//                    wxPayV3Bean.getKeyPath(),
//                    JSONUtil.toJsonStr(refundModel)
//            );
//            // 根据证书序列号查询对应的证书来验证签名结果
//            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
//            log.info("verifySignature: {}", verifySignature);
//            log.info("退款响应 {}", response);
//
//            if (verifySignature) {
//                return response.getBody();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return e.getMessage();
//        }
//        return null;
//    }

    /**
     * 微信退款查询
     */
    @RequestMapping(value = "/refundQueryH5", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
   
    public String refundQueryH5(@RequestParam(required = false) String transactionId,//微信订单号
                                @RequestParam(required = false) String outTradeNo,//商户订单号
                                @RequestParam(required = false) String outRefundNo,//商户退款单号
                                @RequestParam(required = false) String refundId) {//微信退款单号
        if (StringUtils.isBlank(transactionId) && StringUtils.isBlank(outTradeNo) && StringUtils.isBlank(outRefundNo) && StringUtils.isBlank(refundId)) {
            return "transactionId,outTradeNo,outRefundNo,refundId四选一";
        }
        Map<String, String> params = RefundQueryModel.builder()
                .appid(wxPayV3Bean.getAppId())
                .mch_id(wxPayV3Bean.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(transactionId)
                .out_trade_no(outTradeNo)
                .out_refund_no(outRefundNo)
                .refund_id(refundId)
                .build()
                .createSign(wxPayV3Bean.getApiKey(), SignType.MD5);

        return WxPayApi.orderRefundQuery(false, params);
    }

    /**
     * 退款通知
     */
    @RequestMapping(value = "/refundNotifyH5", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
   
    public String refundNotifyH5(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.info("退款通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        if (WxPayKit.codeIsOk(returnCode)) {
            String reqInfo = params.get("req_info");
            String decryptData = WxPayKit.decryptData(reqInfo, wxPayV3Bean.getApiKey());
            log.info("退款通知解密后的数据=" + decryptData);
            // 更新订单信息
            // 发送通知等
            Map<String, String> xml = new HashMap<String, String>(2);
            xml.put("return_code", "SUCCESS");
            xml.put("return_msg", "OK");
            return WxPayKit.toXml(xml);
        }
        return null;
    }

}
