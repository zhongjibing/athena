package com.icezhg.athena.controller;

import com.icezhg.athena.vo.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhongjibing on 2021/10/15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public Object userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();

        UserInfo userInfo = new UserInfo();
        userInfo.setName(oidcUser.getName());
        userInfo.setNickname(oidcUser.getNickName());
        userInfo.setProfile(oidcUser.getProfile());
        userInfo.setPicture(oidcUser.getPicture());
        List<String> authorities = oidcUser.getAuthorities().stream()
                .filter(auth -> auth instanceof OidcUserAuthority)
                .map(GrantedAuthority::getAuthority)
                .toList();
        userInfo.setAuthorities(authorities);
        return userInfo;
    }
}
