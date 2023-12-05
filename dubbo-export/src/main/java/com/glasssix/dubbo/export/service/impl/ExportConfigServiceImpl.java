package com.glasssix.dubbo.export.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glasssix.dubbo.export.domain.po.ExportConfig;
import com.glasssix.dubbo.export.domain.utils.ExportConfigUtil;
import com.glasssix.dubbo.export.domain.vo.ExportConfigPageVO;
import com.glasssix.dubbo.export.domain.vo.ExportConfigVO;
import com.glasssix.dubbo.export.mapper.ExportConfigMapper;
import com.glasssix.dubbo.export.service.ExportConfigService;
import com.glasssix.dubbo.service.RedisService;
import com.glasssix.dubbo.utils.ArrayHelperBase;
import com.glasssix.dubbo.utils.ExportSupport;
import com.glasssix.dubbo.utils.NoModelDataListener;
import com.glasssix.dubbo.utils.PageUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Glasssix
 * @description 针对表【exportConfig(数据导出配置表)】的数据库操作Service实现
 * @createDate 2022-05-19 14:30:38
 */
@Slf4j
@Service
public class ExportConfigServiceImpl extends ServiceImpl<ExportConfigMapper, ExportConfig>
        implements ExportConfigService {

    //针对表【export_config(数据导出配置表)】的数据库操作Mapper
    @Autowired
    ExportConfigMapper exportConfigMapper;

    //针对表【export_config(数据导出配置表)】的缓存操作Service
    @Autowired
    RedisService<ExportConfig> exportConfigRedisService;


    @Override
    public boolean save(ExportConfigVO exportConfigVO) {
        log.info("新增数据导出配置表,参数：{}", exportConfigVO);
        Assert.notNull(exportConfigVO, "参数不能为空!");
        boolean flag = false;
        try {
            ExportConfig exportConfig = ExportConfigUtil.toExportConfig(exportConfigVO);
            flag = exportConfigMapper.insert(exportConfig) > 0;
            exportConfigRedisService.setOneDayExp(ExportConfig.REDIS_KEY + exportConfigVO.getId(), exportConfigVO);
        } catch (Exception e) {
            log.error("新增异常");
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean removeById(Long id) {
        boolean flag = false;
        try {
            ExportConfig exportConfig = exportConfigMapper.selectById(id);
            Assert.notNull(exportConfig, "数据不存在!");
            log.warn("数据不存在:{}", id);
            flag = exportConfigMapper.deleteById(id) > 0;
        } catch (Exception e) {
            log.error("根据ID单个删除异常");
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean removeByIds(List<Long> idList) {
        boolean flag = false;
        try {
            if (CollectionUtils.isEmpty(idList)) {
                log.warn("参数数组为空:{}", idList);
            }
            flag = exportConfigMapper.deleteBatchIds(idList) > 0;
        } catch (Exception e) {
            log.error("根据ID批量删除异常");
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean update(ExportConfigVO exportConfigVO) {
        boolean flag = false;
        Assert.notNull(exportConfigVO.getId(), "参数不能为空!");
        try {
            ExportConfig exportConfig = ExportConfigUtil.toExportConfig(exportConfigVO);
            Assert.notNull(exportConfig, "更新的数据不存在!");
            exportConfig = ExportConfigUtil.toExportConfig(exportConfigVO);
            flag = exportConfigMapper.updateById(exportConfig) > 0;
        } catch (Exception e) {
            log.error("根据ID更新");
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public ExportConfigVO getById(Long id) {
        Assert.notNull(id, "参数不能为空!");
        try {
            ExportConfig exportConfig = exportConfigRedisService.get(ExportConfig.REDIS_KEY + id);
            if (null == exportConfig) {
                exportConfig = exportConfigMapper.selectById(id);
                if (null != exportConfig) {
                    exportConfigRedisService.setOneDayExp(ExportConfig.REDIS_KEY + id, exportConfig);
                }
            }
            Assert.notNull(exportConfig, "数据不存在!");
            return ExportConfigUtil.toExportConfigVO(exportConfig);
        } catch (Exception e) {
            log.error("根据ID获取数据异常");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IPage<ExportConfigVO> page(ExportConfigPageVO exportConfigPageVO) {
        Page<ExportConfig> page = new Page(exportConfigPageVO.getOffset(), exportConfigPageVO.getLimit());
        IPage<ExportConfig> iPage = exportConfigMapper.selectPage(page, getQueryWrapper(exportConfigPageVO));
        return PageUtils.convertIPage(iPage, iPage.getRecords());
    }

    @Override
    public void exportExportConfig(ExportConfigPageVO exportConfigPageVO, String exportKey) {
        List<ExportConfig> exportConfigList = Lists.newArrayList();
        QueryWrapper<ExportConfig> lambdaQueryWrapper = getQueryWrapper(exportConfigPageVO);
        lambdaQueryWrapper.select("*");
        exportConfigMapper.exportData(lambdaQueryWrapper.lambda(), resultContext -> exportConfigList.add(resultContext.getResultObject()));
        ExportConfig exportConfig = exportConfigMapper.selectById(exportKey);
        String fileName = exportConfig.getFilename();
        String[] excelHeaders = exportConfig.getHeaders().split(",");
        String[] excelFields = exportConfig.getFields().split(",");
        try {
            ExportSupport.export(exportConfigList, ExportConfig.class, fileName, excelHeaders, excelFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importExportConfig(InputStream inputStream) {
        List<Map<Integer, String>> objects = EasyExcelFactory.read(inputStream, new NoModelDataListener()).excelType(ExcelTypeEnum.XLSX).sheet(0).headRowNumber(1).doReadSync();
        for (Map<Integer, String> object : objects) {
            System.err.println(object);
        }
        List<ExportConfig> exportConfigList = Lists.newArrayList();
        //TODO
        // importDataList 转换 exportConfigList
        List<List<ExportConfig>> splitList = ArrayHelperBase.splitList(exportConfigList);
        for (List<ExportConfig> list : splitList) {
            super.saveBatch(list);
        }

    }

    @Override
    public List<ExportConfigVO> list(ExportConfigVO exportConfigVO) {
        QueryWrapper<ExportConfig> queryWrapper = new QueryWrapper<>();
        //id
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigVO.getId()), ExportConfig::getId, exportConfigVO.getId());
        //标题头
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigVO.getHeaders()), ExportConfig::getHeaders, exportConfigVO.getHeaders());
        //文件名称
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigVO.getFilename()), ExportConfig::getFilename, exportConfigVO.getFilename());
        //导出字段
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigVO.getFields()), ExportConfig::getFields, exportConfigVO.getFields());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigVO.getExportKey()), ExportConfig::getExportKey, exportConfigVO.getExportKey());
        List<ExportConfig> list = exportConfigMapper.selectList(queryWrapper);
        List<ExportConfigVO> exportConfigVOList = ExportConfigUtil.toExportConfigVOList(list);
        return exportConfigVOList;
    }

    /**
     * 查询条件封装
     *
     * @param exportConfigPageVO
     * @return
     */
    QueryWrapper<ExportConfig> getQueryWrapper(ExportConfigPageVO exportConfigPageVO) {
        QueryWrapper<ExportConfig> queryWrapper = new QueryWrapper<>();
        //id
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigPageVO.getId()), ExportConfig::getId, exportConfigPageVO.getId());
        //标题头
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigPageVO.getHeaders()), ExportConfig::getHeaders, exportConfigPageVO.getHeaders());
        //文件名称
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigPageVO.getFilename()), ExportConfig::getFilename, exportConfigPageVO.getFilename());
        //导出字段
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigPageVO.getFields()), ExportConfig::getFields, exportConfigPageVO.getFields());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(exportConfigPageVO.getExportKey()), ExportConfig::getExportKey, exportConfigPageVO.getExportKey());
        return queryWrapper;
    }
}