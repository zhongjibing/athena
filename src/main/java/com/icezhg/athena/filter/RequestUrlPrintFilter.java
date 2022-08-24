package com.icezhg.athena.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhongjibing on 2021/01/16
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestUrlPrintFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestUrlPrintFilter.class);

    private final ThreadLocal<StopWatch> watch = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String query = request.getQueryString();
        String url = request.getRequestURL().toString() + (StringUtils.isBlank(query) ? "" : "?" + query);
        logger.info("handle[0]: {} {}", request.getMethod(), url);

        watch.set(new StopWatch());
        watch.get().start();

        try {
            chain.doFilter(request, response);
        } finally {
            watch.get().stop();
            logger.info("handle[1]: {} {}, status: {}, request time: {} ms.",
                    request.getMethod(), url, response.getStatus(), watch.get().getTotalTimeMillis());
        }
    }
}
