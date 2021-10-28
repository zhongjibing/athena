package com.icezhg.athena.security;

import com.icezhg.athena.security.filter.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhongjibing on 2020/04/27
 */
public final class ApiLoginConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractAuthenticationFilterConfigurer<H, ApiLoginConfigurer<H>, JsonUsernamePasswordAuthenticationFilter> {

    public ApiLoginConfigurer() {
        super(new JsonUsernamePasswordAuthenticationFilter(), "/signin");
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, RequestMethod.POST.name());
    }
}
