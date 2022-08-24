package com.icezhg.athena.security.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongjibing on 2022/08/25.
 */
public class ClientLogoutPropertiesRegistrationAdapter {

    public static Map<String, ClientLogoutRegistration> getClientRegistrations(ClientLogoutProviderProperties properties) {
        Map<String, ClientLogoutRegistration> registrations = new HashMap<>();
        properties.getRegistration().forEach((key, value) -> registrations.put(key, getClientRegistration(key, value,
                properties.getProvider())));
        return registrations;
    }

    private static ClientLogoutRegistration getClientRegistration(String registrationId,
            ClientLogoutProviderProperties.Registration properties,
            Map<String, ClientLogoutProviderProperties.Provider> providers) {

        ClientLogoutRegistration registration = new ClientLogoutRegistration();
        registration.setRegistrationId(registrationId);
        registration.setClientId(properties.getClientId());
        registration.setProvider(properties.getProvider());
        ClientLogoutProviderProperties.Provider provider = providers.get(properties.getProvider());
        if (provider != null) {
            registration.setUserLogoutUri(provider.getUserLogoutUri());
        }
        return registration;
    }
}
