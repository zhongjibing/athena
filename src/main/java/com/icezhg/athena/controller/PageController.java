package com.icezhg.athena.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhongjibing on 2021/10/27
 */
@Controller
public class PageController {


    @GetMapping({"/"})
    public String home(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getSession().getId());
        return "redirect:http://localhost:8080/";
    }
}
