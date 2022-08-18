package com.icezhg.athena.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2021/10/15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public Object current() {
        return userInfo();
    }

    @GetMapping("/info")
    public Object userInfo() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
