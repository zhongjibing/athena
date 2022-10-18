package com.icezhg.athena.controller.monitor;

import com.icezhg.athena.service.monitor.OnlineUserService;
import com.icezhg.athena.vo.query.OnlineUserQuery;
import com.icezhg.athena.vo.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor/online")
public class OnlineUserController {

    private final OnlineUserService onlineUserService;

    public OnlineUserController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @GetMapping("/list")
    public PageResult list(OnlineUserQuery query) {
        return onlineUserService.listOnlineUsers(query);
    }


}
