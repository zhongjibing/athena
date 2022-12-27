package com.icezhg.athena.config;

import com.icezhg.authorization.core.config.AuthorizeRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by zhongjibing on 2022/09/13.
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthorizeRequestCustomizer<HttpSecurity> customAuthorizeRequestCustomizer() {
        return authorizeRequests -> authorizeRequests
                .requestMatchers("/actuator/*").permitAll()
                .requestMatchers("/authenticated").permitAll()
                .requestMatchers("/favicon.*", "/css/**", "/fonts/**", "/img/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/picture/*").permitAll()
                .anyRequest().authenticated();
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        System.out.println(passwordEncoder.encode("@zeuskb34M2051"));
    }
}
