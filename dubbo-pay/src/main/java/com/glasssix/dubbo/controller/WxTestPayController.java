package com.glasssix.dubbo.controller;


import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.glasssix.dubbo.utils.Result;
import com.glasssix.dubbo.service.WxPayService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@CrossOrigin //跨域
@RestController
@RequestMapping("/api/wx-pay")
@Slf4j
public class WxTestPayController {

    @Resource
    private WxPayService wxPayService;


    @RequestMapping("/getSerialNumber")
    @ResponseBody
    public Result serialNumber() {
        return Result.ok(wxPayService.getMchSerialNumber());
    }

    @RequestMapping("/getPlatSerialNumber")
    @ResponseBody
    public Result platSerialNumber() {
        return Result.ok(wxPayService.getPlatSerialNumber());
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result v3Get() {
        // 获取平台证书列表
        return Result.ok(wxPayService.getPlatformCertificate());
    }

    /**
     * Native下单
     *
     * @param productId
     * @return
     * @throws Exception
     */
    @PostMapping("/native/{productId}")
    public Result nativePay(@PathVariable Long productId) throws Exception {
        log.info("发起支付请求 v3");
        //返回支付二维码连接和订单号
        Map<String, Object> map = wxPayService.nativePay(productId);
        return Result.ok(map);
    }

    @RequestMapping(value = "/payNotify", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        wxPayService.payNotify(request, response);
    }


    /**
     * 用户取消订单
     *
     * @param orderNo
     * @return
     * @throws
     */
    @PostMapping("/cancel/{orderNo}")
    public Result cancel(@PathVariable String orderNo) throws Exception {
        log.info("取消订单");
        wxPayService.cancelOrder(orderNo);
        return Result.ok(true, "订单已取消");
    }

    /**
     * 查询订单
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    @GetMapping("/query/{orderNo}")
    public Result queryOrder(@PathVariable String orderNo) throws Exception {
        log.info("查询订单");
        String result = wxPayService.queryOrder(orderNo);
        Map<String, Object> map = Maps.newHashMap();
        map.put("result", result);
        return Result.ok(map);

    }

    //
//
    @PostMapping("/refunds/{orderNo}/{reason}")
    public Result refunds(@PathVariable String orderNo, @PathVariable String reason) throws Exception {
        log.info("申请退款");
        wxPayService.refund(orderNo, reason);
        return Result.ok();
    }

    /**
     * 查询退款
     *
     * @param refundNo
     * @return
     * @throws Exception
     */
    @GetMapping("/query-refund/{refundNo}")
    public Result queryRefund(@PathVariable String refundNo) throws Exception {

        log.info("查询退款");
        String result = wxPayService.queryRefund(refundNo);
        Map<String, Object> map = Maps.newHashMap();
        map.put("result", result);
        return Result.ok(map);
    }


    /**
     * 退款结果通知
     * 退款状态改变后，微信会把相关退款结果发送给商户。
     */
    @PostMapping("/refunds/notify")
    public void refundsNotify(HttpServletRequest request, HttpServletResponse response) {
        wxPayService.refundsNotify(request, response);
    }

    @GetMapping("/querybill/{billDate}/{type}")
    public Result queryTradeBill(
            @PathVariable String billDate,
            @PathVariable String type) throws Exception {

        log.info("获取账单url");

        String downloadUrl = wxPayService.queryBill(billDate, type);
        Map<String, Object> map = Maps.newHashMap();
        map.put("downloadUrl", downloadUrl);
        return Result.ok(map);
    }

    @GetMapping("/downloadbill/{billDate}/{type}")
    public Result downloadBill(
            @PathVariable String billDate,
            @PathVariable String type) throws Exception {

        log.info("下载账单");
        String result = wxPayService.downloadBill(billDate, type);
        Map<String, Object> map = Maps.newHashMap();
        map.put("result", result);
        return Result.ok(map);
    }

}
