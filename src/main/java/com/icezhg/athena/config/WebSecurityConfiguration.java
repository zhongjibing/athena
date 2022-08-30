package com.icezhg.athena.config;

import com.icezhg.athena.security.logout.Oauth2LogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collections;

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
                        logout
                                .logoutUrl("/logout")
                                .defaultLogoutSuccessHandlerFor(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT), AnyRequestMatcher.INSTANCE)
                                .addLogoutHandler(new Oauth2LogoutHandler())
                                .deleteCookies("SESSION")
                                .invalidateHttpSession(true)
                )
                .exceptionHandling(httpSecurity ->
                        httpSecurity
                                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint(getLoginUrl(http)),
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

    @SuppressWarnings("unchecked")
    private String getLoginUrl(HttpSecurity http) {
        Iterable<ClientRegistration> clientRegistrations = Collections.emptyList();
        ClientRegistrationRepository clientRegistrationRepository = ConfigurerUtils.getSharedObject(http, ClientRegistrationRepository.class);
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        if (clientRegistrations != null) {
            for (ClientRegistration registration : clientRegistrations) {
                if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(registration.getAuthorizationGrantType())) {
                    return OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + registration.getRegistrationId();
                }
            }
        }

        return "/login";
    }

}
