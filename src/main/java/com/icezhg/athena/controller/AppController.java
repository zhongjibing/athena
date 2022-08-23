package com.icezhg.athena.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by zhongjibing on 2021/10/27
 */
@Slf4j
@Controller
@RefreshScope
public class AppController {

    @Value("${home-page:/}")
    private String homePage;

    @GetMapping("/")
    public String index() {
        log.info("redirect: {}", homePage);
        return "redirect:" + homePage;
    }
}
