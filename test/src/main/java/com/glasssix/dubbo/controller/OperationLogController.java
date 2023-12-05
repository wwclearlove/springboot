package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.domain.OperationLog;
import com.glasssix.dubbo.service.OperationLogService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【operation_log(操作日志表)】的数据库操作Controller
 * @createDate 2023-01-15 14:53:00
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class OperationLogController {

    //针对表【(operation_log)】的数据库操作Service

    private final OperationLogService operationLogService;

    /**
     * 列表查询
     *
     * @param  operationLog
     * @return
     */
    @GetMapping("/operation-logs")
    public List<OperationLog> list(OperationLog operationLog) {
        return operationLogService.list(operationLog);
    }

}
