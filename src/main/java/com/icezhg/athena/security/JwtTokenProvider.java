package com.icezhg.athena.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhongjibing on 2020/04/27
 */
@Slf4j
//@Component
//@RefreshScope
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private static final long TOKEN_EXPIRE_MILLIS = 30 * 60 * 1000L;

    @Value("${jwt.secret-key:SecretKey0123456789}")
    private String secretKey;

    public String createToken(Authentication authentication) {
        return JWT.create()
                .withSubject(authentication.getName())
                .withClaim(AUTHORITIES_KEY, StringUtils.arrayToCommaDelimitedString(authentication.getAuthorities().toArray()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRE_MILLIS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("invalid token, " + e.getMessage());
        }

        String auth = decodedJWT.getClaims().get(AUTHORITIES_KEY).asString();
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(auth);
        User principal = new User(decodedJWT.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validate(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("Invalid token, {}", e.getMessage());
        }
        return false;
    }
}
