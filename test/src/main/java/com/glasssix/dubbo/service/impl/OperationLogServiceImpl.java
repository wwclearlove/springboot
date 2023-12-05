package com.glasssix.dubbo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glasssix.dubbo.mapper.OperationLogMapper;
import com.glasssix.dubbo.service.OperationLogService;
import com.glasssix.dubbo.domain.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【operationLog(操作日志表)】的数据库操作Service实现
 * @createDate 2023-01-15 14:53:00
 */
@Slf4j
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
        implements OperationLogService {

    //针对表【operation_log(操作日志表)】的数据库操作Mapper
    @Autowired
    OperationLogMapper operationLogMapper;

    @Override
    public List<OperationLog> list(OperationLog operationLog) {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        //主键 id
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getId()), OperationLog::getId, operationLog.getId());
        //主动创建时间
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getCreateTime()), OperationLog::getCreateTime, operationLog.getCreateTime());
        //操作时间
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getOperationTime()), OperationLog::getOperationTime, operationLog.getOperationTime());
        //操作 ip
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getIp()), OperationLog::getIp, operationLog.getIp());
        //用户 id
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getUserId()), OperationLog::getUserId, operationLog.getUserId());
        //ip归属地
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getIpHome()), OperationLog::getIpHome, operationLog.getIpHome());
        //操作
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getOperation()), OperationLog::getOperation, operationLog.getOperation());
        //操作系统
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getOs()), OperationLog::getOs, operationLog.getOs());
        //浏览器
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getBrowser()), OperationLog::getBrowser, operationLog.getBrowser());
        //链接
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getOperationUrl()), OperationLog::getOperationUrl, operationLog.getOperationUrl());
        //用户名称
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(operationLog.getUsername()), OperationLog::getUsername, operationLog.getUsername());
        List<OperationLog> list = operationLogMapper.selectList(queryWrapper);
        return list;
    }
}