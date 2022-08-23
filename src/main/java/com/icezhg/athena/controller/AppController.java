package com.icezhg.athena.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zhongjibing on 2021/10/27
 */
@Controller
@RefreshScope
public class AppController {
    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @Value("${home-page:/}")
    private String homePage;

    @GetMapping("/")
    public String index() {
        log.info("redirect: {}", homePage);
        return "redirect:" + homePage;
    }

    @RequestMapping("/authenticated")
    public Object authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication != null && authentication.getPrincipal() != null;
    }
}
