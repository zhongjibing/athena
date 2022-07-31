package com.icezhg.athena.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by zhongjibing on 2022/07/30.
 */
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(authorize ->
                        authorize
                                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                                .pathMatchers("/favicon.ico").permitAll()
                                .anyExchange().authenticated()
                )
                .oauth2Login();

        return http.build();
    }
}
