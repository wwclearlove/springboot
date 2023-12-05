package com.glasssix.dubbo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glasssix.dubbo.domain.Product;
import com.glasssix.dubbo.mapper.ProductMapper;
import com.glasssix.dubbo.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
