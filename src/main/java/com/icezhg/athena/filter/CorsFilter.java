package com.icezhg.athena.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Created by zhongjibing on 2021/10/16
 */
//@Component("webCorsFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter //extends OncePerRequestFilter
         {
//    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info("handle request: {}", request.getRequestURI());
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        filterChain.doFilter(request, response);
//    }
}
