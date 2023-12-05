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
import com.glasssix.dubbo.domain.utils.Aggregate3Util;
import com.glasssix.dubbo.domain.vo.Aggregate3PageVO;
import com.glasssix.dubbo.domain.Aggregate3;
import com.glasssix.dubbo.service.Aggregate3Service;
import com.glasssix.dubbo.mapper.Aggregate3Mapper;
import com.glasssix.dubbo.domain.vo.Aggregate3VO;
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
 * @description 针对表【aggregate3(OLAP)】的数据库操作Service实现
 * @createDate 2022-11-09 16:12:07
 */
@Slf4j
@Service
public class Aggregate3ServiceImpl extends ServiceImpl<Aggregate3Mapper, Aggregate3>
        implements Aggregate3Service {

    //针对表【aggregate3(OLAP)】的数据库操作Mapper
    @Autowired
    Aggregate3Mapper aggregate3Mapper;

    //针对表【export_config(数据导出配置表)】的数据库操作Mapper
    @Autowired
    ExportConfigMapper exportConfigMapper;


    @Override
    public boolean save(Aggregate3VO aggregate3VO) {
        log.info("新增OLAP,参数：{}", aggregate3VO);
        Assert.notNull(aggregate3VO, "参数不能为空!");
        boolean flag = false;
        Aggregate3 aggregate3 = Aggregate3Util.toAggregate3(aggregate3VO);
        flag = aggregate3Mapper.insert(aggregate3) > 0;
        return flag;
    }

    @Override
    public boolean removeById(Long id) {
        boolean flag = false;
        Aggregate3 aggregate3 = aggregate3Mapper.selectById(id);
        Assert.notNull(aggregate3, "数据不存在!");
        log.warn("数据不存在:{}", id);
        flag = aggregate3Mapper.deleteById(id) > 0;
        return flag;
    }

    @Override
    public boolean removeByIds(List<Long> idList) {
        boolean flag = false;
        if (CollectionUtils.isEmpty(idList)) {
            log.warn("参数数组为空:{}", idList);
        }
        flag = aggregate3Mapper.deleteBatchIds(idList) > 0;
        return flag;
    }

    @Override
    public boolean update(Aggregate3VO aggregate3VO) {
        boolean flag = false;
        Aggregate3 aggregate3 = Aggregate3Util.toAggregate3(aggregate3VO);
        Assert.notNull(aggregate3, "更新的数据不存在!");
        aggregate3 = Aggregate3Util.toAggregate3(aggregate3VO);
        flag = aggregate3Mapper.updateById(aggregate3) > 0;

        return flag;
    }

    @Override
    public Aggregate3VO getById(Long id) {
        Assert.notNull(id, "参数不能为空!");
        Aggregate3 aggregate3 = aggregate3Mapper.selectById(id);
        Assert.notNull(aggregate3, "数据不存在!");
        return Aggregate3Util.toAggregate3VO(aggregate3);
    }

    @Override
    public IPage<Aggregate3VO> page(Aggregate3PageVO aggregate3PageVO) {
        Page<Aggregate3> page = new Page(aggregate3PageVO.getOffset(), aggregate3PageVO.getLimit());
        IPage<Aggregate3> iPage = aggregate3Mapper.selectPage(page, getQueryWrapper(aggregate3PageVO));
        return PageUtils.convertIPage(iPage, iPage.getRecords());
    }

    @Override
    public void exportAggregate3(Aggregate3PageVO aggregate3PageVO, String exportKey) {
        List<Aggregate3> aggregate3List = Lists.newArrayList();
        QueryWrapper<Aggregate3> lambdaQueryWrapper = getQueryWrapper(aggregate3PageVO);
        lambdaQueryWrapper.select("*");
        aggregate3Mapper.exportData(lambdaQueryWrapper.lambda(), resultContext -> aggregate3List.add(resultContext.getResultObject()));
        ExportConfig exportConfig = exportConfigMapper.selectById(exportKey);
        String fileName = exportConfig.getFilename();
        String[] excelHeaders = exportConfig.getHeaders().split(",");
        String[] excelFields = exportConfig.getFields().split(",");
        try {
            ExportSupport.export(aggregate3List, Aggregate3.class, fileName, excelHeaders, excelFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importAggregate3(InputStream inputStream) {
        List<Map<Integer, String>> objects = EasyExcelFactory.read(inputStream, new NoModelDataListener()).excelType(ExcelTypeEnum.XLSX).sheet(0).headRowNumber(1).doReadSync();
        for (Map<Integer, String> object : objects) {
            System.err.println(object);
        }
        List<Aggregate3> aggregate3List = Lists.newArrayList();
        //TODO
        // importDataList 转换 aggregate3List
        List<List<Aggregate3>> splitList = ArrayHelperBase.splitList(aggregate3List);
        for (List<Aggregate3> list : splitList) {
            super.saveBatch(list);
        }

    }

    @Override
    public List<Aggregate3VO> list(Aggregate3VO aggregate3VO) {
        QueryWrapper<Aggregate3> queryWrapper = new QueryWrapper<>();
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3VO.getCreateTime()), Aggregate3::getCreateTime, aggregate3VO.getCreateTime());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3VO.getType()), Aggregate3::getType, aggregate3VO.getType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3VO.getAreaId()), Aggregate3::getAreaId, aggregate3VO.getAreaId());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3VO.getHealthCode()), Aggregate3::getHealthCode, aggregate3VO.getHealthCode());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3VO.getPv()), Aggregate3::getPv, aggregate3VO.getPv());
        List<Aggregate3> list = aggregate3Mapper.selectList(queryWrapper);
        List<Aggregate3VO> aggregate3VOList = Aggregate3Util.toAggregate3VOList(list);
        return aggregate3VOList;
    }

    /**
     * 查询条件封装
     *
     * @param aggregate3PageVO
     * @return
     */
    QueryWrapper<Aggregate3> getQueryWrapper(Aggregate3PageVO aggregate3PageVO) {
        QueryWrapper<Aggregate3> queryWrapper = new QueryWrapper<>();
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3PageVO.getCreateTime()), Aggregate3::getCreateTime, aggregate3PageVO.getCreateTime());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3PageVO.getType()), Aggregate3::getType, aggregate3PageVO.getType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3PageVO.getAreaId()), Aggregate3::getAreaId, aggregate3PageVO.getAreaId());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3PageVO.getHealthCode()), Aggregate3::getHealthCode, aggregate3PageVO.getHealthCode());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(aggregate3PageVO.getPv()), Aggregate3::getPv, aggregate3PageVO.getPv());
        return queryWrapper;
    }
}