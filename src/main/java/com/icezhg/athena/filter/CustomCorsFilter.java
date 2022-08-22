package com.icezhg.athena.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhongjibing on 2021/10/16
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class CustomCorsFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CustomCorsFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (CorsUtils.isCorsRequest(request)) {
            String origin = request.getHeader("Origin");
            log.info("CORS request origin: [{}]{}", request.getMethod(), origin);
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Access-Control-Expose-Headers,Token,isToken");

            if (HttpMethod.OPTIONS.matches(request.getMethod())) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return;
            }
        }

        chain.doFilter(request, response);
    }

}
