package com.icezhg.athena.controller.monitor;

import com.icezhg.athena.service.monitor.LoginRecordService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.query.LoginQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor/login")
public class LoginRecordController {

    private final LoginRecordService loginRecordService;

    public LoginRecordController(LoginRecordService loginRecordService) {
        this.loginRecordService = loginRecordService;
    }

    @GetMapping("/records")
    public PageResult listLogs(LoginQuery query) {
        return new PageResult(loginRecordService.count(query), loginRecordService.find(query));
    }
}
