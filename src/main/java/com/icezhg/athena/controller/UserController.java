package com.icezhg.athena.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhongjibing on 2021/10/15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/hello")
    public Mono<Object> hello() {
        return Mono.just("hello, world!");
    }

    @GetMapping("/curuser")
    public Mono<Object> current() {
        return Mono.just(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @GetMapping("/is-login")
    public Mono<Object> isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLogin = authentication != null && authentication.isAuthenticated() && (!AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass()));
        return Mono.just(isLogin);
    }

    @GetMapping("/info")
    public Mono<Object> userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", authentication.getName());
        userInfo.put("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return Mono.just(userInfo);
    }
}
