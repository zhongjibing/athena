package com.icezhg.athena.security.filter;

import com.icezhg.athena.security.JwtTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhongjibing on 2020/04/27
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";

    public static final String AUTHORIZATION = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = resolveToken(request);

        if (StringUtils.isNotBlank(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (this.jwtTokenProvider.validate(jwt)) {
                Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        String jwt = request.getParameter(AUTHORIZATION);
        if (org.springframework.util.StringUtils.hasText(jwt)) {
            return jwt;
        }
        return null;
    }
}
