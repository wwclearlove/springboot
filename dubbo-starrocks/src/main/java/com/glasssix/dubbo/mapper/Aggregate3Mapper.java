package com.glasssix.dubbo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.glasssix.dubbo.domain.Aggregate3;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate3(OLAP)】的数据库操作Mapper
 * @createDate 2022-11-09 16:12:07
 * @Entity com.glasssix.dubbo.domain.Aggregate3;
 */
public interface Aggregate3Mapper extends BaseMapper<Aggregate3> {

/**
 * 导出专用
 *
 * @param wrapper
 * @param handler
 */
    @Select("SELECT ${ew.sqlSelect} FROM aggregate3 ${ew.customSqlSegment} limit 0,200000")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    @ResultType(Aggregate3.class)
    void exportData(@Param(Constants.WRAPPER)  LambdaQueryWrapper<Aggregate3> wrapper, ResultHandler<Aggregate3> handler);
}




