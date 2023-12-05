package com.glasssix.dubbo.utils;


import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.glasssix.dubbo.vo.InputPage;

import java.util.List;

/**
 * @program: java-nwpu
 * @description:
 * @author: wenjiang
 * @create: 2020-04-24 09:06
 **/
public class PageUtils {

    static final String ORDER_ASC = "ASC";
    static final String ORDER_DESC = "DESC";


    public static IPage emptyPage() {
        return new IPage() {
            @Override
            public List<OrderItem> orders() {
                return Lists.newArrayList();
            }

            @Override
            public List getRecords() {
                return Lists.newArrayList();
            }

            @Override
            public IPage setRecords(List records) {
                return null;
            }

            @Override
            public long getTotal() {
                return 0;
            }

            @Override
            public IPage setTotal(long total) {
                return null;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public IPage setSize(long size) {
                return null;
            }

            @Override
            public long getCurrent() {
                return 0;
            }

            @Override
            public IPage setCurrent(long current) {
                return null;
            }
        };
    }

    /**
     * 替换page的分页内容
     * @param page
     * @param list
     * @param <T>
     * @return
     */
    public static <T> IPage<T> convertIPage(IPage page, List list) {
        IPage iPage = new Page();
        iPage.setTotal(page.getTotal());
        iPage.setSize(page.getSize());
        iPage.setPages(page.getPages());
        iPage.setCurrent(page.getCurrent());
        iPage.setRecords(list);
        return iPage;
    }

    public static QueryWrapper buildOrder(QueryWrapper queryWrapper, String columns, String orders) {
        InputPage inputPage = new InputPage();
        inputPage.setSortby(columns);
        inputPage.setOrder(orders);
        return buildOrder(queryWrapper, inputPage);
    }

    public static QueryWrapper buildOrder(QueryWrapper queryWrapper, InputPage inputPage) {
        String columns = inputPage.getSortby();
        String orders = inputPage.getOrder();
        if (!columns.isEmpty() && !orders.isEmpty()) {
            columns += ",id";
            orders += ",asc";
            String[] columnList = columns.split(",");
            String[] orderList = orders.split(",");
            if (columnList.length != orderList.length) {
                throw new IllegalArgumentException("排序规则和属性不对应");
            }
            for (int x = 0; x < columnList.length; x++) {
                String column = columnList[x];
                String order;
                boolean isAsc;

                order = orderList[x].toUpperCase();
                if (!(order.equals(ORDER_ASC) || order.equals(ORDER_DESC))) {
                    throw new IllegalArgumentException("非法的排序规则：" + column);
                } else {
                    isAsc = order.equals(ORDER_ASC) ? true : false;
                }
                if (!column.matches("[A-Za-z0-9_]+")) {
                    throw new IllegalArgumentException("非法的排序字段名称：" + column);
                }
                queryWrapper.orderBy(true, isAsc, humpConversionUnderscore(column));
            }
        }
        return queryWrapper;
    }


    public static String humpConversionUnderscore(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = value.toCharArray();
        for (char charactor : chars) {
            if (Character.isUpperCase(charactor)) {
                stringBuilder.append("_");
                charactor = Character.toLowerCase(charactor);
            }
            stringBuilder.append(charactor);
        }
        return stringBuilder.toString();
    }
}