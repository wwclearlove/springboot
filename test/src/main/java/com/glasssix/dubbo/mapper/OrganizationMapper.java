package com.glasssix.dubbo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.glasssix.dubbo.domain.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;


/**
 * @author Glasssix-LJT
 * @description 针对表【organization】的数据库操作Mapper
 * @createDate 2023-01-30 10:15:33
 * @Entity com.glasssix.dubbo.domain.Organization;
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

/**
 * 导出专用
 *
 * @param wrapper
 * @param handler
 */
    @Select("SELECT ${ew.sqlSelect} FROM organization ${ew.customSqlSegment} limit 0,346070")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    @ResultType(Organization.class)
    void exportData(@Param(Constants.WRAPPER)  LambdaQueryWrapper<Organization> wrapper, ResultHandler<Organization> handler);

    @Select("SELECT  ${ew.sqlSelect} FROM organization  ${ew.customSqlSegment} limit 0,346070 ")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    Page<Organization> listData(@Param("page") Page<Organization> page,@Param(Constants.WRAPPER) QueryWrapper<Organization> queryWrapper);
}




