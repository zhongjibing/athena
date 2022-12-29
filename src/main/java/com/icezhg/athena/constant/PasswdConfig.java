package com.icezhg.athena.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zhongjibing on 2022/12/28.
 */
@Component
public class PasswdConfig {

    private static UserPasswdConfig userPasswdConfig;

    private static ClientPasswdConfig clientPasswdConfig;

    public PasswdConfig(UserPasswdConfig userPasswdConfig, ClientPasswdConfig clientPasswdConfig) {
        PasswdConfig.userPasswdConfig = userPasswdConfig;
        PasswdConfig.clientPasswdConfig = clientPasswdConfig;
    }

    public static String userInitPasswd() {
        return userPasswdConfig.passwd;
    }

    public static String clientInitPasswd() {
        return clientPasswdConfig.passwd;
    }

    @Component
    @ConfigurationProperties(prefix = "sys.user.init")
    public static class UserPasswdConfig {
        private String passwd;

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
    }

    @Component
    @ConfigurationProperties(prefix = "sys.client.init")
    public static class ClientPasswdConfig {
        private String passwd;

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
    }
}
