package com.glasssix.dubbo.export.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glasssix.dubbo.export.domain.po.ExportConfig;
import com.glasssix.dubbo.export.domain.vo.ExportConfigPageVO;
import com.glasssix.dubbo.export.domain.vo.ExportConfigVO;

import java.io.InputStream;
import java.util.List;

/**
 * @author Glasssix
 * @description 针对表【export_config(数据导出配置表)】的数据库操作Service
 * @createDate 2022-05-19 14:30:38
 */
public interface ExportConfigService extends IService<ExportConfig> {

    /**
     * 新增
     *
     * @param exportConfigVO
     * @return
     */
    boolean save(ExportConfigVO exportConfigVO);

    /**
     * 根据ID单个删除
     *
     * @param id
     * @return
     */
    boolean removeById(Long id);

    /**
     * 根据ID批量删除
     *
     * @param idList
     * @return
     */
    boolean removeByIds(List<Long> idList);

    /**
     * 根据ID更新
     *
     * @param exportConfigVO
     * @return
     */
    boolean update(ExportConfigVO exportConfigVO);

    /**
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    ExportConfigVO getById(Long id);

    /**
     * 分页查询
     *
     * @param exportConfigPageVO
     * @return
     */
    IPage<ExportConfigVO> page(ExportConfigPageVO exportConfigPageVO);

    /**
     * 根据分页条件导出
     *
     * @param exportConfigPageVO
     * @param exportKey
     * @return
     */
    void exportExportConfig(ExportConfigPageVO exportConfigPageVO, String exportKey);

    /**
     * 导入
     *
     * @param inputStream
     * @return
     */
    void importExportConfig(InputStream inputStream);

    /**
     * 列表查询
     *
     * @param exportConfigVO
     * @return
     */
    List<ExportConfigVO> list(ExportConfigVO exportConfigVO);
}