package com.icezhg.athena.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2Login().and()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .mvcMatchers("/actuator/*").permitAll()
                                .mvcMatchers("/authenticated").permitAll()
                                .mvcMatchers("/favicon.*", "/css/**", "/fonts/**", "/img/**").permitAll()
                                .anyRequest().authenticated()
                )
                .logout(logout ->
                        logout.logoutUrl("/logout").deleteCookies("SESSION").invalidateHttpSession(true)
                );
        return http.build();
    }
}
