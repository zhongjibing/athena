package com.icezhg.athena.security.util;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Created by zhongjibing on 2022/07/31.
 */
public final class Jwks {

    private Jwks() {
    }

    public static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    public static RSAKey generateRsa(KeyProperties keyProperties) {
        return generateRsa(generateRsaKey(keyProperties));
    }

    public static RSAKey generateRsa(KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    public static KeyPair generateRsaKey(KeyProperties keyProperties) {
        KeyProperties.KeyStore keyStore = keyProperties.getKeyStore();
        if (keyStore.getLocation() != null && keyStore.getLocation().exists()) {
            return new KeyStoreKeyFactory(keyStore.getLocation(), keyStore.getPassword().toCharArray(),
                    keyStore.getType()).getKeyPair(keyStore.getAlias(), keyStore.getSecret().toCharArray());
        }
        return generateRsaKey();
    }
}
