package com.icezhg.athena.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by zhongjibing on 2021/10/27
 */
@Controller
public class PageController {


    @GetMapping({"/"})
    public String home() {
        return "redirect:http://localhost:8080/";
    }
}
