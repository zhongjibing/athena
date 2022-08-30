package com.icezhg.athena.controller;

import com.icezhg.athena.service.UserService;
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
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/authenticated")
    public Object authenticated() {
        return SecurityUtil.authenticated();
    }

    @GetMapping("/user/info")
    public Object userInfo() {
        return SecurityUtil.currentUserInfo();
    }
}
