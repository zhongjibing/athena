package com.icezhg.athena.security;

import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * Created by zhongjibing on 2022/08/05.
 */
public class WebFilterManager {

    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    public WebFilterManager(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public void configure(ServerHttpSecurity http) {
        http.addFilterAfter(new ApiFilter(this.clientRegistrationRepository), SecurityWebFiltersOrder.HTTP_BASIC);
    }
}
