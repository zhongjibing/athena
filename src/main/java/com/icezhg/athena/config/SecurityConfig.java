package com.icezhg.athena.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Created by zhongjibing on 2022/07/30.
 */
@EnableWebFluxSecurity
@EnableWebFlux
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

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable().cors().and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS).permitAll()
//                .anyRequest().authenticated().and()
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .addFilter(new JwtAuthenticationTokenFilter(jwtTokenProvider))
////                .headers().cacheControl()
//        ;
//    }
}
