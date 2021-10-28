package com.icezhg.athena.security.filter;

import com.icezhg.athena.util.JsonUtil;
import com.icezhg.athena.vo.LoginInfo;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by zhongjibing on 2020/04/27
 */
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ThreadLocal<LoginInfo> requestBody = new ThreadLocal<>();

    private void threadLocalInit(HttpServletRequest request) {
        if (requestBody.get() != null) {
            return;
        }

        LoginInfo loginInfo = null;
        if (MediaType.APPLICATION_JSON.isCompatibleWith(MimeTypeUtils.parseMimeType(request.getContentType()))) {
            try {
                loginInfo = JsonUtil.toBean(request.getInputStream(), LoginInfo.class);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        requestBody.set(loginInfo != null ? loginInfo : new LoginInfo());
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        threadLocalInit(request);
        return requestBody.get().getPassword();
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        threadLocalInit(request);
        return requestBody.get().getUsername();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } finally {
            requestBody.remove();
        }
    }
}
