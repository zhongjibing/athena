package com.icezhg.athena.filter;

import com.icezhg.athena.constant.SessionKey;
import com.icezhg.athena.service.common.IpLocationService;
import com.icezhg.authorization.core.util.IpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Created by zhongjibing on 2022/12/24.
 */
@Component
public class SessionPropertiesFilter extends OncePerRequestFilter {

    private final IpLocationService ipLocationService;

    public SessionPropertiesFilter(IpLocationService ipLocationService) {
        this.ipLocationService = ipLocationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String ipAddr = (String) session.getAttribute(SessionKey.REQUEST_IP);
            if (ipAddr == null) {
                ipAddr = IpUtil.getRequestIp();
                session.setAttribute(SessionKey.REQUEST_IP, ipAddr);
            }

            if (session.getAttribute(SessionKey.REQUEST_LOCATION) == null) {
                String ipLocation = ipLocationService.search(ipAddr);
                session.setAttribute(SessionKey.REQUEST_LOCATION, ipLocation);
            }
        }

        chain.doFilter(request, response);
    }
}
