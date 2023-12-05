package com.glasssix.dubbo.export.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.glasssix.dubbo.export.domain.po.ExportConfig;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author Glasssix
 * @description 针对表【export_config(数据导出配置表)】的数据库操作Mapper
 * @createDate 2022-05-19 14:30:38
 * @Entity generator.domain.po.ExportConfig;
 */
public interface ExportConfigMapper extends BaseMapper<ExportConfig> {

    /**
     * 导出专用
     *
     * @param wrapper
     * @param handler
     */
    @Select("SELECT ${ew.sqlSelect} FROM export_config ${ew.customSqlSegment} limit 0,200000")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    @ResultType(ExportConfig.class)
    void exportData(@Param(Constants.WRAPPER) LambdaQueryWrapper<ExportConfig> wrapper, ResultHandler<ExportConfig> handler);
}




