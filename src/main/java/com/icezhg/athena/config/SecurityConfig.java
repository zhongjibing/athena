package com.icezhg.athena.config;

import com.icezhg.athena.security.WebFilterManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Created by zhongjibing on 2022/07/30.
 */
@EnableWebFluxSecurity
@EnableWebFlux
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, WebFilterManager webFilterManager) {
        http
                .authorizeExchange(authorize ->
                        authorize
                                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                                .pathMatchers("/favicon.ico").permitAll()
                                .anyExchange().authenticated()
                )
                .oauth2Login().and()
                .logout();

        webFilterManager.configure(http);

        return http.build();
    }

    @Bean
    WebFilterManager filterManager(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return new WebFilterManager(clientRegistrationRepository);
    }
}
