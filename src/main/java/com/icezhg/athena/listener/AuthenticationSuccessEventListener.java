package com.icezhg.athena.listener;

import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.constant.SessionKey;
import com.icezhg.authorization.core.util.Requests;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

/**
 * Created by zhongjibing on 2022/09/10.
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        HttpServletRequest request = Requests.getRequest();
        if (request != null && event.getAuthentication() != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object principal = event.getAuthentication().getPrincipal();
                if (principal instanceof DefaultOidcUser oidcUser) {
                    session.setAttribute(SessionKey.REQUEST_IP, oidcUser.getAttribute(Constants.ATTRIBUTE_IP));
                    session.setAttribute(SessionKey.REQUEST_LOCATION, oidcUser.getAttribute(Constants.ATTRIBUTE_IP_LOCATION));
                }
            }
        }
    }
}

