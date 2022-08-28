package com.icezhg.athena.config;

import com.icezhg.athena.security.logout.Oauth2LogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final String LOGIN_URL = "/oauth2/authorization/athena";

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2Login().and()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .mvcMatchers("/actuator/*").permitAll()
                                .mvcMatchers("/authenticated", "/user/info").permitAll()
                                .mvcMatchers("/favicon.*", "/css/**", "/fonts/**", "/img/**").permitAll()
                                .anyRequest().authenticated()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .defaultLogoutSuccessHandlerFor(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT), AnyRequestMatcher.INSTANCE)
                                .addLogoutHandler(new Oauth2LogoutHandler())
                                .deleteCookies("SESSION")
                                .invalidateHttpSession(true)
                )
                .exceptionHandling(httpSecurity ->
                        httpSecurity
                                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint(LOGIN_URL),
                                        negatedJsonMediaTypeRequestMatcher())
                                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                                        , jsonMediaTypeRequestMatcher())
                                .defaultAccessDeniedHandlerFor((request, response, ex) -> response.setStatus(HttpStatus.FORBIDDEN.value()),
                                        jsonMediaTypeRequestMatcher())
                                .defaultAccessDeniedHandlerFor(new AccessDeniedHandlerImpl(),
                                        negatedJsonMediaTypeRequestMatcher())
                )
                .headers(headers -> {
                    headers.cacheControl().disable();
                })
                .sessionManagement(session ->
                        session.maximumSessions(1)
                );
        return http.build();
    }

    private RequestMatcher jsonMediaTypeRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON);
        requestMatcher.setUseEquals(true);
        return requestMatcher;
    }

    private RequestMatcher negatedJsonMediaTypeRequestMatcher() {
        return new NegatedRequestMatcher(jsonMediaTypeRequestMatcher());
    }
}
