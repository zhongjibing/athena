package com.icezhg.athena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Filter;
import java.util.List;

/**
 * Created by zhongjibing on 2021/10/15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    List<Filter> filters;

    @GetMapping("/hello")
    public Object hello() {
        return "hello, world!";
    }

    @GetMapping("/curuser")
    public Object current() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
