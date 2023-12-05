package com.glasssix.dubbo.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glasssix.dubbo.domain.Aggregate2;
import com.glasssix.dubbo.service.Aggregate2Service;
import com.glasssix.dubbo.service.AsyncService;
import com.glasssix.dubbo.service.RedisService;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glasssix.dubbo.export.domain.po.ExportConfig;
import com.glasssix.dubbo.export.mapper.ExportConfigMapper;
import com.glasssix.dubbo.utils.ArrayHelperBase;
import com.glasssix.dubbo.utils.ExportSupport;
import com.glasssix.dubbo.utils.NoModelDataListener;
import com.glasssix.dubbo.utils.PageUtils;
import com.google.common.collect.Lists;
import com.glasssix.dubbo.domain.utils.Aggregate1Util;
import com.glasssix.dubbo.domain.vo.Aggregate1PageVO;
import com.glasssix.dubbo.domain.Aggregate1;
import com.glasssix.dubbo.service.Aggregate1Service;
import com.glasssix.dubbo.mapper.Aggregate1Mapper;
import com.glasssix.dubbo.domain.vo.Aggregate1VO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate1(OLAP)】的数据库操作Service实现
 * @createDate 2022-11-09 16:12:07
 */
@Slf4j
@Service
public class Aggregate1ServiceImpl extends ServiceImpl<Aggregate1Mapper, Aggregate1>
        implements Aggregate1Service {

    //针对表【aggregate1(OLAP)】的数据库操作Mapper
    @Autowired
    Aggregate1Mapper aggregate1Mapper;

    //针对表【export_config(数据导出配置表)】的数据库操作Mapper
    @Autowired
    ExportConfigMapper exportConfigMapper;

    //针对表【aggregate1(OLAP)】的缓存操作Service

    @Autowired
    AsyncService asyncService;
    @Autowired
    Aggregate2Service service;
    public  LocalDate randomLong(){
        //格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //输入 随机起始时间
        String str = "2022-06-01";
        //解析时间
        Date d1 = null;
        try {
            d1 = sdf.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long before = d1.getTime();
        //获取当前时间
        Date d2 = new Date();
        long after = d2.getTime();
        Random r = new Random();
        long l = (long) (before + (r.nextFloat() * (after - before + 1)));
        return Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDate();
    }


    @Override
    public boolean saveTest() {
     List<Aggregate1>list= Lists.newArrayList();
        // 异步多线程  插入数据库
        Random r = new Random();
        for (int i = 0; i < 1000000; i++) {
            int type = r.nextInt(2)+1;
//            int deviceType = r.nextInt(10)+1;
            int areaId = (int)(Math.random()*100+1);
            Aggregate1 aggregate2=new Aggregate1(randomLong(),type,areaId,1L);
            list.add(aggregate2);
        }
       saveBatch(list);
        return true;
    }

    @Override
    public boolean save(Aggregate1VO aggregate1VO) {
        log.info("新增OLAP,参数：{}", aggregate1VO);
        Assert.notNull(aggregate1VO, "参数不能为空!");
        boolean flag = false;
        Aggregate1 aggregate1 = Aggregate1Util.toAggregate1(aggregate1VO);
        flag = aggregate1Mapper.insert(aggregate1) > 0;
        return flag;
    }

    @Override
    public boolean removeById(Long id) {
        boolean flag = false;
        Aggregate1 aggregate1 = aggregate1Mapper.selectById(id);
        Assert.notNull(aggregate1, "数据不存在!");
        log.warn("数据不存在:{}", id);
        flag = aggregate1Mapper.deleteById(id) > 0;

        return flag;
    }

    @Override
    public boolean removeByIds(List<Long> idList) {
        boolean flag = false;
        if (CollectionUtils.isEmpty(idList)) {
            log.warn("参数数组为空:{}", idList);
        }
        flag = aggregate1Mapper.deleteBatchIds(idList) > 0;

        return flag;
    }

    @Override
    public boolean update(Aggregate1VO aggregate1VO) {
        boolean flag = false;
        Aggregate1 aggregate1 = Aggregate1Util.toAggregate1(aggregate1VO);
        Assert.notNull(aggregate1, "更新的数据不存在!");
        aggregate1 = Aggregate1Util.toAggregate1(aggregate1VO);
        flag = aggregate1Mapper.updateById(aggregate1) > 0;

        return flag;
    }

    @Override
    public Aggregate1VO getById(Long id) {
        Assert.notNull(id, "参数不能为空!");
        Aggregate1 aggregate1 = aggregate1Mapper.selectById(id);
        Assert.notNull(aggregate1, "数据不存在!");
        return Aggregate1Util.toAggregate1VO(aggregate1);
    }

    @Override
    public IPage<Aggregate1VO> page(Aggregate1PageVO aggregate1PageVO) {
        Page<Aggregate1> page = new Page(aggregate1PageVO.getOffset(), aggregate1PageVO.getLimit());
        IPage<Aggregate1> iPage = aggregate1Mapper.selectPage(page, getQueryWrapper(aggregate1PageVO));
        return PageUtils.convertIPage(iPage, iPage.getRecords());
    }

    @Override
    public void exportAggregate1(Aggregate1PageVO aggregate1PageVO, String exportKey) {
        List<Aggregate1> aggregate1List = Lists.newArrayList();
        QueryWrapper<Aggregate1> lambdaQueryWrapper = getQueryWrapper(aggregate1PageVO);
        lambdaQueryWrapper.select("*");
        aggregate1Mapper.exportData(lambdaQueryWrapper.lambda(), resultContext -> aggregate1List.add(resultContext.getResultObject()));
        ExportConfig exportConfig = exportConfigMapper.selectById(exportKey);
        String fileName = exportConfig.getFilename();
        String[] excelHeaders = exportConfig.getHeaders().split(",");
        String[] excelFields = exportConfig.getFields().split(",");
        try {
            ExportSupport.export(aggregate1List, Aggregate1.class, fileName, excelHeaders, excelFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importAggregate1(InputStream inputStream) {
        List<Map<Integer, String>> objects = EasyExcelFactory.read(inputStream, new NoModelDataListener()).excelType(ExcelTypeEnum.XLSX).sheet(0).headRowNumber(1).doReadSync();
        for (Map<Integer, String> object : objects) {
            System.err.println(object);
        }
        List<Aggregate1> aggregate1List = Lists.newArrayList();
        //TODO
        // importDataList 转换 aggregate1List
        List<List<Aggregate1>> splitList = ArrayHelperBase.splitList(aggregate1List);
        for (List<Aggregate1> list : splitList) {
            super.saveBatch(list);
        }

    }

    @Override
    public List<Aggregate1VO> list(Aggregate1VO aggregate1VO) {
        QueryWrapper<Aggregate1> queryWrapper = new QueryWrapper<>();
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1VO.getCreatedTime()), Aggregate1::getCreatedTime, aggregate1VO.getCreatedTime());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1VO.getType()), Aggregate1::getType, aggregate1VO.getType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1VO.getAreaId()), Aggregate1::getAreaId, aggregate1VO.getAreaId());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1VO.getPv()), Aggregate1::getPv, aggregate1VO.getPv());
        List<Aggregate1> list = aggregate1Mapper.selectList(queryWrapper);
        List<Aggregate1VO> aggregate1VOList = Aggregate1Util.toAggregate1VOList(list);
        return aggregate1VOList;
    }

    /**
     * 查询条件封装
     *
     * @param aggregate1PageVO
     * @return
     */
    QueryWrapper<Aggregate1> getQueryWrapper(Aggregate1PageVO aggregate1PageVO) {
        QueryWrapper<Aggregate1> queryWrapper = new QueryWrapper<>();
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1PageVO.getCreateTime()), Aggregate1::getCreatedTime, aggregate1PageVO.getCreateTime());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1PageVO.getType()), Aggregate1::getType, aggregate1PageVO.getType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1PageVO.getAreaId()), Aggregate1::getAreaId, aggregate1PageVO.getAreaId());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate1PageVO.getPv()), Aggregate1::getPv, aggregate1PageVO.getPv());
        return queryWrapper;
    }
}