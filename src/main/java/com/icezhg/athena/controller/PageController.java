package com.icezhg.athena.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by zhongjibing on 2021/10/27
 */
@Controller
public class PageController {

    @Value("${front.home-page}")
    private String frontHomePage;

    @GetMapping({"/"})
    public String home() {
        return "redirect:" + this.frontHomePage;
    }
}
