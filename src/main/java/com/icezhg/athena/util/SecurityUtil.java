package com.icezhg.athena.util;

import com.icezhg.athena.vo.UserInfo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SecurityUtil {

    public static boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            CsrfToken csrfToken =
                    (CsrfToken) servletRequestAttributes.getRequest().getAttribute(CsrfToken.class.getName());
            HttpServletResponse response = servletRequestAttributes.getResponse();
            if (csrfToken != null && response != null) {
                response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
            }
        }
        return true;
    }

    public static UserInfo currentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return UserInfo.builder()
                    .name(oidcUser.getName())
                    .nickname(oidcUser.getNickName())
                    .picture(oidcUser.getPicture())
                    .profile(oidcUser.getProfile())
                    .authorities(oidcUser
                            .getAuthorities().stream()
                            .filter(auth -> auth instanceof OidcUserAuthority)
                            .map(GrantedAuthority::getAuthority)
                            .toList())
                    .build();
        }

        return UserInfo.builder()
                .name("anonymousUser")
                .authorities(List.of("ROLE_ANONYMOUS"))
                .build();
    }
}
