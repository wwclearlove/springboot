package com.glasssix.dubbo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.glasssix.dubbo.domain.Aggregate1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate1(OLAP)】的数据库操作Mapper
 * @createDate 2022-11-09 16:12:07
 * @Entity com.glasssix.dubbo.domain.Aggregate1;
 */
public interface Aggregate1Mapper extends BaseMapper<Aggregate1> {

/**
 * 导出专用
 *
 * @param wrapper
 * @param handler
 */
    @Select("SELECT ${ew.sqlSelect} FROM aggregate1 ${ew.customSqlSegment} limit 0,200000")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    @ResultType(Aggregate1.class)
    void exportData(@Param(Constants.WRAPPER)  LambdaQueryWrapper<Aggregate1> wrapper, ResultHandler<Aggregate1> handler);
}




