package com.icezhg.athena.security.client;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhongjibing on 2022/08/25.
 */
@Getter
@Setter
public class ClientLogoutRegistration {

    private String registrationId;

    private String clientId;

    private String provider;

    private String userLogoutUri;
}
