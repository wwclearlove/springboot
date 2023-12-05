package com.glasssix.dubbo.service.impl;

import com.alibaba.excel.EasyExcelFactory;
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
import com.glasssix.dubbo.domain.utils.Aggregate2Util;
import com.glasssix.dubbo.domain.vo.Aggregate2PageVO;
import com.glasssix.dubbo.domain.Aggregate2;
import com.glasssix.dubbo.service.Aggregate2Service;
import com.glasssix.dubbo.mapper.Aggregate2Mapper;
import com.glasssix.dubbo.domain.vo.Aggregate2VO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate2(OLAP)】的数据库操作Service实现
 * @createDate 2022-11-09 16:12:07
 */
@Slf4j
@Service
public class Aggregate2ServiceImpl extends ServiceImpl<Aggregate2Mapper, Aggregate2>
        implements Aggregate2Service{

    //针对表【aggregate2(OLAP)】的数据库操作Mapper
    @Autowired
    Aggregate2Mapper aggregate2Mapper;

    //针对表【export_config(数据导出配置表)】的数据库操作Mapper
    @Autowired
    ExportConfigMapper exportConfigMapper;




    @Override
    public boolean save(Aggregate2VO aggregate2VO) {
        log.info("新增OLAP,参数：{}", aggregate2VO);
        Assert.notNull(aggregate2VO, "参数不能为空!");
        boolean flag = false;
        Aggregate2 aggregate2 = Aggregate2Util.toAggregate2(aggregate2VO);
        flag = aggregate2Mapper.insert(aggregate2) > 0;
        return flag;
    }

    @Override
    public boolean removeById(Long id) {
        boolean flag = false;
        Aggregate2 aggregate2 = aggregate2Mapper.selectById(id);
        Assert.notNull(aggregate2, "数据不存在!");
        log.warn("数据不存在:{}", id);
        flag = aggregate2Mapper.deleteById(id) > 0;
        return flag;
    }

    @Override
    public boolean removeByIds(List<Long> idList) {
        boolean flag = false;
        if (CollectionUtils.isEmpty(idList)) {
        log.warn("参数数组为空:{}", idList);
        }
        flag = aggregate2Mapper.deleteBatchIds(idList) > 0;
        return flag;
    }

    @Override
    public boolean update(Aggregate2VO aggregate2VO) {
        boolean flag = false;
        Aggregate2 aggregate2 = Aggregate2Util.toAggregate2(aggregate2VO);
        Assert.notNull(aggregate2, "更新的数据不存在!");
        aggregate2 = Aggregate2Util.toAggregate2(aggregate2VO);
        flag = aggregate2Mapper.updateById(aggregate2) > 0;
        return flag;
    }

    @Override
    public Aggregate2VO getById(Long id) {
        Assert.notNull(id, "参数不能为空!");
            Aggregate2  aggregate2 = aggregate2Mapper.selectById(id);
        Assert.notNull(aggregate2, "数据不存在!");
        return Aggregate2Util.toAggregate2VO(aggregate2);
    }

    @Override
    public IPage<Aggregate2VO> page(Aggregate2PageVO aggregate2PageVO) {
    Page<Aggregate2> page = new Page(aggregate2PageVO.getOffset(), aggregate2PageVO.getLimit());
        IPage<Aggregate2> iPage = aggregate2Mapper.selectPage(page, getQueryWrapper(aggregate2PageVO));
        return PageUtils.convertIPage(iPage, iPage.getRecords());
    }

    @Override
    public void exportAggregate2(Aggregate2PageVO aggregate2PageVO, String exportKey) {
        List<Aggregate2> aggregate2List = Lists.newArrayList();
        QueryWrapper<Aggregate2> lambdaQueryWrapper = getQueryWrapper(aggregate2PageVO);
        lambdaQueryWrapper.select("*");
        aggregate2Mapper.exportData(lambdaQueryWrapper.lambda(), resultContext -> aggregate2List.add(resultContext.getResultObject()));
        ExportConfig exportConfig = exportConfigMapper.selectById(exportKey);
        String fileName = exportConfig.getFilename();
        String[] excelHeaders = exportConfig.getHeaders().split(",");
        String[] excelFields = exportConfig.getFields().split(",");
        try {
            ExportSupport.export(aggregate2List, Aggregate2.class, fileName, excelHeaders, excelFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importAggregate2(InputStream inputStream) {
        List<Map<Integer, String>> objects = EasyExcelFactory.read(inputStream, new NoModelDataListener()).excelType(ExcelTypeEnum.XLSX).sheet(0).headRowNumber(1).doReadSync();
        for (Map<Integer, String> object : objects) {
            System.err.println(object);
        }
        List<Aggregate2> aggregate2List = Lists.newArrayList();
        //TODO
        // importDataList 转换 aggregate2List
        List<List<Aggregate2>> splitList = ArrayHelperBase.splitList(aggregate2List);
        for (List<Aggregate2> list : splitList) {
            super.saveBatch(list);
        }

    }

    @Override
    public List<Aggregate2VO> list(Aggregate2VO aggregate2VO) {
        QueryWrapper<Aggregate2> queryWrapper = new QueryWrapper<>();
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2VO.getCreateTime()), Aggregate2::getCreateTime, aggregate2VO.getCreateTime());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2VO.getType()), Aggregate2::getType, aggregate2VO.getType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2VO.getAreaId()), Aggregate2::getAreaId, aggregate2VO.getAreaId());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2VO.getDeviceType()), Aggregate2::getDeviceType, aggregate2VO.getDeviceType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2VO.getPv()), Aggregate2::getPv, aggregate2VO.getPv());
        List<Aggregate2> list = aggregate2Mapper.selectList(queryWrapper);
        List<Aggregate2VO> aggregate2VOList = Aggregate2Util.toAggregate2VOList(list);
        return aggregate2VOList;
    }

    /**
     * 查询条件封装
     *
     * @param aggregate2PageVO
     * @return
     */
    QueryWrapper<Aggregate2> getQueryWrapper(Aggregate2PageVO aggregate2PageVO) {
        QueryWrapper<Aggregate2> queryWrapper = new QueryWrapper<>();
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2PageVO.getCreateTime()), Aggregate2::getCreateTime, aggregate2PageVO.getCreateTime());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2PageVO.getType()), Aggregate2::getType, aggregate2PageVO.getType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2PageVO.getAreaId()), Aggregate2::getAreaId, aggregate2PageVO.getAreaId());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2PageVO.getDeviceType()), Aggregate2::getDeviceType, aggregate2PageVO.getDeviceType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate2PageVO.getPv()), Aggregate2::getPv, aggregate2PageVO.getPv());
        return queryWrapper;
    }
}