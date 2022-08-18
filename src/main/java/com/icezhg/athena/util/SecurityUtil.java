package com.icezhg.athena.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class SecurityUtil {

    public static String curUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String name) {
                return name;
            } else if (principal instanceof Principal pri) {
                return pri.getName();
            }
        }
        return null;
    }
}
