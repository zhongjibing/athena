package com.icezhg.athena.controller;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.service.system.UserService;
import com.icezhg.authorization.core.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2021/10/15
 */
@Slf4j
@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/authenticated")
    public Object authenticated() {
        return SecurityUtil.authenticated();
    }

    @GetMapping("/user/info")
    @Operation(title = "current user info", type = OperationType.QUERY)
    public Object userInfo() {
        return SecurityUtil.currentUserInfo();
    }
}
