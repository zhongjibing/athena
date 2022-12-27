package com.icezhg.athena.service.system.impl;

import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.constant.SysConfig;
import com.icezhg.athena.dao.ClientDao;
import com.icezhg.athena.domain.Client;
import com.icezhg.athena.service.system.ClientService;
import com.icezhg.athena.service.system.ConfigService;
import com.icezhg.athena.vo.ClientInfo;
import com.icezhg.athena.vo.ClientPasswd;
import com.icezhg.athena.vo.ClientStatus;
import com.icezhg.athena.vo.query.ClientQuery;
import com.icezhg.athena.vo.query.Query;
import com.icezhg.authorization.core.SecurityUtil;
import com.icezhg.authorization.core.util.JsonUtil;
import com.icezhg.commons.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhongjibing on 2022/12/25.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    private ConfigService configService;

    private PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean checkUnique(ClientInfo client) {
        ClientQuery query = new ClientQuery();
        query.setClientName(client.getClientName());
        List<ClientInfo> clients = find(query);
        return clients.isEmpty() || (clients.size() == 1 && Objects.equals(client.getId(), clients.get(0).getId()));
    }

    @Override
    public ClientInfo save(ClientInfo client) {
        Client newClient = new Client();
        newClient.setId(IdGenerator.nextId());
        newClient.setClientId(client.getClientName());
        newClient.setClientName(client.getClientName());
        newClient.setClientIdIssuedAt(new Date());
        newClient.setClientSecret(passwordEncoder.encode(defaultPasswd()));
        newClient.setClientSecretExpiresAt(null);
        newClient.setClientAuthenticationMethods(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue());
        newClient.setAuthorizationGrantTypes(client.getAuthorizationGrantTypes());
        newClient.setRedirectUris(client.getRedirectUris());
        newClient.setScopes(client.getScopes());
        newClient.setClientSettings(JsonUtil.toJson(ClientSettings.getInstance().getSettings()));
        newClient.setTokenSettings(JsonUtil.toJson(TokenSettings.getInstance().getSettings()));
        newClient.setStatus(client.getStatus());
        newClient.setRemark(client.getRemark());

        String currentUser = SecurityUtil.currentUserName();
        newClient.setCreateBy(currentUser);
        newClient.setUpdateBy(currentUser);
        newClient.setCreateTime(new Date());
        newClient.setUpdateTime(new Date());

        clientDao.insert(newClient);
        return findById(client.getId());
    }

    @Override
    public ClientInfo update(ClientInfo client) {
        Client newClient = new Client();
        newClient.setId(client.getId());
        newClient.setClientId(client.getClientName());
        newClient.setClientName(client.getClientName());
        newClient.setAuthorizationGrantTypes(client.getAuthorizationGrantTypes());
        newClient.setRedirectUris(client.getRedirectUris());
        newClient.setScopes(client.getScopes());
        newClient.setStatus(client.getStatus());
        newClient.setRemark(client.getRemark());

        String currentUser = SecurityUtil.currentUserName();
        newClient.setUpdateBy(currentUser);
        newClient.setUpdateTime(new Date());

        clientDao.update(newClient);
        return findById(client.getId());
    }

    @Override
    public ClientInfo findById(String id) {
        return clientDao.findById(id);
    }

    @Override
    public int count(Query query) {
        return clientDao.count(query.toMap());
    }

    @Override
    public List<ClientInfo> find(Query query) {
        return clientDao.find(query.toMap());
    }

    @Override
    public int changeStatus(ClientStatus clientStatus) {
        Client newClient = new Client();
        newClient.setId(clientStatus.getId());
        newClient.setStatus(clientStatus.getStatus());
        newClient.setUpdateBy(SecurityUtil.currentUserName());
        newClient.setUpdateTime(new Date());
        return clientDao.update(newClient);
    }

    @Override
    public int resetPasswd(ClientPasswd clientPasswd) {
        Client newClient = new Client();
        newClient.setId(clientPasswd.getId());
        newClient.setClientSecret(passwordEncoder.encode(clientPasswd.getPasswd()));
        newClient.setUpdateBy(SecurityUtil.currentUserName());
        newClient.setUpdateTime(new Date());
        return clientDao.update(newClient);
    }

    private String defaultPasswd() {
        String initClientPasswd = configService.findConfig(SysConfig.INIT_CLIENT_PASSWD);
        return StringUtils.defaultString(initClientPasswd, Constants.DEFAULT_CLIENT_PASSWD);
    }


    private static abstract class AbstractSettings<B extends AbstractSettings<B>> {
        static final String SETTINGS_NAMESPACE = "settings.";
        private final Map<String, Object> settings = new LinkedHashMap<>();

        public Map<String, Object> getSettings() {
            return Collections.unmodifiableMap(this.settings);
        }

        public B setting(String name, Object value) {
            Assert.hasText(name, "name cannot be empty");
            Assert.notNull(value, "value cannot be null");
            settings.put(name, value);
            return getThis();
        }

        @SuppressWarnings("unchecked")
        protected final B getThis() {
            return (B) this;
        }

    }

    private static class ClientSettings extends AbstractSettings<ClientSettings> {
        private static final String CLIENT_SETTINGS_NAMESPACE = SETTINGS_NAMESPACE + "client.";
        private static final String REQUIRE_PROOF_KEY = CLIENT_SETTINGS_NAMESPACE + "require-proof-key";
        private static final String REQUIRE_AUTHORIZATION_CONSENT = CLIENT_SETTINGS_NAMESPACE + "require" +
                "-authorization-consent";
        private static final String JWK_SET_URL = CLIENT_SETTINGS_NAMESPACE + "jwk-set-url";
        private static final String TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM = CLIENT_SETTINGS_NAMESPACE +
                "token-endpoint-authentication-signing-algorithm";

        private ClientSettings() {
            super
                    .setting(REQUIRE_PROOF_KEY, false)
                    .setting(REQUIRE_AUTHORIZATION_CONSENT, false);
        }

        public static ClientSettings getInstance() {
            return new ClientSettings();
        }

        public ClientSettings requireProofKey(boolean requireProofKey) {
            return setting(REQUIRE_PROOF_KEY, requireProofKey);
        }

        public ClientSettings requireAuthorizationConsent(boolean requireAuthorizationConsent) {
            return setting(REQUIRE_AUTHORIZATION_CONSENT, requireAuthorizationConsent);
        }

        public ClientSettings jwkSetUrl(String jwkSetUrl) {
            return setting(JWK_SET_URL, jwkSetUrl);
        }

        public ClientSettings tokenEndpointAuthenticationSigningAlgorithm(JwsAlgorithm authenticationSigningAlgorithm) {
            return setting(TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM, authenticationSigningAlgorithm);
        }
    }

    private static class TokenSettings extends AbstractSettings<TokenSettings> {
        private static final String TOKEN_SETTINGS_NAMESPACE = SETTINGS_NAMESPACE.concat("token.");
        private static final String AUTHORIZATION_CODE_TIME_TO_LIVE =
                TOKEN_SETTINGS_NAMESPACE.concat("authorization" + "-code-time-to-live");
        private static final String ACCESS_TOKEN_TIME_TO_LIVE = TOKEN_SETTINGS_NAMESPACE.concat("access-token-time-to" +
                "-live");
        private static final String ACCESS_TOKEN_FORMAT = TOKEN_SETTINGS_NAMESPACE.concat("access-token-format");
        private static final String REUSE_REFRESH_TOKENS = TOKEN_SETTINGS_NAMESPACE.concat("reuse-refresh-tokens");
        private static final String REFRESH_TOKEN_TIME_TO_LIVE = TOKEN_SETTINGS_NAMESPACE.concat("refresh-token-time" +
                "-to-live");
        private static final String ID_TOKEN_SIGNATURE_ALGORITHM = TOKEN_SETTINGS_NAMESPACE.concat("id-token" +
                "-signature-algorithm");

        private TokenSettings() {
            super
                    .setting(REUSE_REFRESH_TOKENS, true)
                    .setting(ID_TOKEN_SIGNATURE_ALGORITHM, SignatureAlgorithm.RS256)
                    .setting(ACCESS_TOKEN_TIME_TO_LIVE, Duration.ofMinutes(5))
                    .setting(ACCESS_TOKEN_FORMAT, OAuth2TokenFormat.SELF_CONTAINED)
                    .setting(REFRESH_TOKEN_TIME_TO_LIVE, Duration.ofMinutes(60))
                    .setting(AUTHORIZATION_CODE_TIME_TO_LIVE, Duration.ofMinutes(5));
        }

        public static TokenSettings getInstance() {
            return new TokenSettings();
        }

        public TokenSettings authorizationCodeTimeToLive(Duration authorizationCodeTimeToLive) {
            Assert.notNull(authorizationCodeTimeToLive, "authorizationCodeTimeToLive cannot be null");
            Assert.isTrue(authorizationCodeTimeToLive.getSeconds() > 0, "authorizationCodeTimeToLive must be greater " +
                    "than Duration.ZERO");
            return setting(AUTHORIZATION_CODE_TIME_TO_LIVE, authorizationCodeTimeToLive);
        }


        public TokenSettings accessTokenTimeToLive(Duration accessTokenTimeToLive) {
            Assert.notNull(accessTokenTimeToLive, "accessTokenTimeToLive cannot be null");
            Assert.isTrue(accessTokenTimeToLive.getSeconds() > 0, "accessTokenTimeToLive must be greater than " +
                    "Duration.ZERO");
            return setting(ACCESS_TOKEN_TIME_TO_LIVE, accessTokenTimeToLive);
        }

        public TokenSettings accessTokenSelfContained() {
            return setting(ACCESS_TOKEN_FORMAT, OAuth2TokenFormat.SELF_CONTAINED);
        }

        public TokenSettings accessTokenReference() {
            return setting(ACCESS_TOKEN_FORMAT, OAuth2TokenFormat.REFERENCE);
        }

        public TokenSettings reuseRefreshTokens(boolean reuseRefreshTokens) {
            return setting(REUSE_REFRESH_TOKENS, reuseRefreshTokens);
        }


        public TokenSettings refreshTokenTimeToLive(Duration refreshTokenTimeToLive) {
            Assert.notNull(refreshTokenTimeToLive, "refreshTokenTimeToLive cannot be null");
            Assert.isTrue(refreshTokenTimeToLive.getSeconds() > 0, "refreshTokenTimeToLive must be greater than " +
                    "Duration.ZERO");
            return setting(REFRESH_TOKEN_TIME_TO_LIVE, refreshTokenTimeToLive);
        }


        public TokenSettings idTokenSignatureAlgorithm(SignatureAlgorithm idTokenSignatureAlgorithm) {
            Assert.notNull(idTokenSignatureAlgorithm, "idTokenSignatureAlgorithm cannot be null");
            return setting(ID_TOKEN_SIGNATURE_ALGORITHM, idTokenSignatureAlgorithm);
        }
    }

}
