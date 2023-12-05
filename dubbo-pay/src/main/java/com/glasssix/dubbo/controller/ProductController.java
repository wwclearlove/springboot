package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.domain.Product;
import com.glasssix.dubbo.service.ProductService;
import com.glasssix.dubbo.utils.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin //开放前端的跨域访问
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    private ProductService productService;


    @GetMapping("/list")
    public Result list(){
        List<Product> list = productService.list();
        return Result.ok(list);
    }
}
