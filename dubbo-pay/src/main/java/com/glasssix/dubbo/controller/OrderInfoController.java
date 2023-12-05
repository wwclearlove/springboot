package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.domain.OrderInfo;
import com.glasssix.dubbo.enums.OrderStatus;
import com.glasssix.dubbo.service.OrderInfoService;
import com.glasssix.dubbo.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin //开放前端的跨域访问
@RestController
@RequestMapping("/api/order-info")
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;

    @GetMapping("/list")
    public Result list() {
        List<OrderInfo> list = orderInfoService.listOrderByCreateTimeDesc();
        return Result.ok(list);
    }

    /**
     * 查询本地订单状态
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/query-order-status/{orderNo}")
    public Result queryOrderStatus(@PathVariable String orderNo) {
        String orderStatus = orderInfoService.getOrderStatus(orderNo);
        if (OrderStatus.SUCCESS.getType().equals(orderStatus)) {
            return Result.ok(true,"支付成功");
        }
        return Result.error(101,"支付中");
    }
}
