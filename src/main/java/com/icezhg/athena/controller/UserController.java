package com.icezhg.athena.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2021/10/15
 */
@RestController
public class UserController {

    @RequestMapping("/authenticated")
    public Object authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication != null && authentication.getPrincipal() != null;
    }

    @GetMapping("/user/info")
    public Object userInfo() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
