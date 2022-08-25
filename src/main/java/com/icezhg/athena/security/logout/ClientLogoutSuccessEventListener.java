package com.icezhg.athena.security.logout;

import com.icezhg.athena.security.client.ClientLogoutPropertiesRegistrationAdapter;
import com.icezhg.athena.security.client.ClientLogoutProviderProperties;
import com.icezhg.athena.security.client.ClientLogoutRegistration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhongjibing on 2022/08/25.
 */
@Slf4j
@Component
public class ClientLogoutSuccessEventListener implements ApplicationListener<LogoutSuccessEvent> {

    private static final String ACCESS_TOKEN = "access_token";

    private final RestOperations restOperations = new RestTemplate();
    private final Map<String, ClientLogoutRegistration> clientRegistrations;

    public ClientLogoutSuccessEventListener(ClientLogoutProviderProperties properties) {
        this.clientRegistrations = ClientLogoutPropertiesRegistrationAdapter.getClientRegistrations(properties);
    }

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        if (event.getAuthentication() instanceof OAuth2AuthenticationToken authentication) {
            String registrationId = authentication.getAuthorizedClientRegistrationId();
            ClientLogoutRegistration registration = clientRegistrations.get(registrationId);
            if (Objects.nonNull(registration) && StringUtils.hasText(registration.getUserLogoutUri())) {
                logout(authentication, registration);
            }
        }
    }

    private void logout(OAuth2AuthenticationToken authentication, ClientLogoutRegistration registration) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String bearer = "Bearer " + ((OidcUser) authentication.getPrincipal()).getIdToken().getTokenValue();
        headers.put(HttpHeaders.AUTHORIZATION, List.of(bearer));

        URI uri = UriComponentsBuilder.fromUriString(registration.getUserLogoutUri()).build().toUri();
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.POST, uri);
        ResponseEntity<Object> response = restOperations.exchange(requestEntity, Object.class);
        System.out.println(response.getStatusCode());
    }
}
