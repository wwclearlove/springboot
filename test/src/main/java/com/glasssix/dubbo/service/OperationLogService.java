package com.glasssix.dubbo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glasssix.dubbo.domain.OperationLog;

import java.util.List;
/**
 * @author Glasssix-LJT
 * @description 针对表【operation_log(操作日志表)】的数据库操作Service
 * @createDate 2023-01-15 14:53:00
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     *
     * 列表查询
     *
     * @param operationLog
     * @return
     */
    List<OperationLog> list(OperationLog operationLog);
}