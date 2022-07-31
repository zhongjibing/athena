package com.icezhg.athena.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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

    @GetMapping
    public Mono<Object> current() {
        return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal());
    }

    @GetMapping("/info")
    public Mono<Object> userInfo() {
        return ReactiveSecurityContextHolder.getContext().map(context -> {
            Authentication authentication = context.getAuthentication();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", authentication.getName());
            userInfo.put("roles",
                    authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            return userInfo;
        });
    }
}
