package com.icezhg.athena.controller.monitor;

import com.icezhg.athena.service.monitor.OperationLogService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.query.OperationLogQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2022/12/24.
 */
@RestController
@RequestMapping("/monitor/operation/history")
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping("/list")
    public PageResult listLogs(OperationLogQuery query) {
        return new PageResult(operationLogService.count(query), operationLogService.find(query));
    }
}
