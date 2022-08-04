package com.icezhg.athena.security;

import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created by zhongjibing on 2022/08/05.
 */
public class ApiFilter implements WebFilter {
    private final ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver;

    public ApiFilter(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.authorizationRequestResolver =
                new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        LogFactory.getLog(this.getClass()).info("apifilter");
        return chain.filter(exchange).onErrorResume(Exception.class, e -> {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        });
    }
}
