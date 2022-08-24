package com.icezhg.athena.security.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongjibing on 2022/08/25.
 */
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class ClientLogoutProviderProperties {

    private final Map<String, Registration> registration = new HashMap<>();

    private final Map<String, Provider> provider = new HashMap<>();

    public Map<String, Registration> getRegistration() {
        return registration;
    }

    public Map<String, Provider> getProvider() {
        return provider;
    }

    @Getter
    @Setter
    public static class Registration {
        private String provider;
        private String clientId;
    }

    @Getter
    @Setter
    public static class Provider {
        private String userLogoutUri;
    }
}
