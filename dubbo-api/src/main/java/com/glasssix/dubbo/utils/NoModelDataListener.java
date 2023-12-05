package com.glasssix.dubbo.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author: 骑猪撞地球QAQ
 * @date: 2022/3/28 15:34
 * @content: 动态表头数据解析监听器
 */
@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {


    List<Map<String, Object>> addList = new ArrayList<>();

    /**
     * 存储Key
     */
    Map<Integer, String> key = new HashMap<>();
    /**
     * keuList
     */
    List<String> keyList = new ArrayList<>();

    /**
     * 重写invokeHeadMap方法，获去表头，如果有需要获取第一行表头就重写这个方法，不需要则不需要重写
     *
     * @param headMap Excel每行解析的数据为Map<Integer, String>类型，Integer是Excel的列索引,String为Excel的单元格值
     * @param context context能获取一些东西，比如context.readRowHolder().getRowIndex()为Excel的行索引，表头的行索引为0，0之后的都解析成数据
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条头数据：{}, currentRowHolder: {}", headMap.toString(), context.readRowHolder().getRowIndex());
        Set<Integer> integerSet = headMap.keySet();
        for (Integer integer : integerSet) {
            keyList.add(headMap.get(integer));
        }
        key.putAll(headMap);
    }


    /**
     * 读取数据 筛选过滤数据 赛选出那些数据是添加 那些数据是修改
     *
     * @param data    传入数据
     * @param context 读取数据
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        Set<Integer> integerSet = data.keySet();
        String name = "";
        for (Integer integer : integerSet) {
            String s = data.get(integer);
            if (integer == 0) {
                name = data.get(integer);
            } else {
                // 第一格为战区、部门名称数据，不做校验
                boolean number = this.isNumber(data.get(integer));
                if (!number) {
                    try {
                        throw new Exception(name + "数据格式错误，请调整后重试！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            objectObjectHashMap.put(key.get(integer), s);
        }
        addList.add(objectObjectHashMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 是否是数字或小数位不超过两位
     *
     * @param str 需要校验字符串
     * @return true：是；false：不是
     */
    private boolean isNumber(String str) {
        String reg = "\\d+(\\.\\d+)?";
        if (str.matches(reg)) {
            String ss[];
            if (str.contains(".")) {
                ss = str.split("\\.");
                String s = ss[1];
                return s.length() <= 2;
            }
        }
        return str.matches(reg);
    }
}
