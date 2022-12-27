package org.springframework.security.oauth2.server.authorization.settings;

import org.springframework.util.Assert;

/**
 * Created by zhongjibing on 2022/12/25.
 */
public final class OAuth2TokenFormat {

    /**
     * Self-contained tokens use a protected, time-limited data structure that contains token metadata
     * and claims of the user and/or client. JSON Web Token (JWT) is a widely used format.
     */
    public static final OAuth2TokenFormat SELF_CONTAINED = new OAuth2TokenFormat("self-contained");

    /**
     * Reference (opaque) tokens are unique identifiers that serve as a reference
     * to the token metadata and claims of the user and/or client, stored at the provider.
     */
    public static final OAuth2TokenFormat REFERENCE = new OAuth2TokenFormat("reference");

    private final String value;

    /**
     * Constructs an {@code OAuth2TokenFormat} using the provided value.
     *
     * @param value the value of the token format
     */
    public OAuth2TokenFormat(String value) {
        Assert.hasText(value, "value cannot be empty");
        this.value = value;
    }

    /**
     * Returns the value of the token format.
     *
     * @return the value of the token format
     */
    public String getValue() {
        return this.value;
    }
}
