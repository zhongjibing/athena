package com.icezhg.athena.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.icezhg.authorization.core.serial.AuthenticationExceptionMixIn;
import com.icezhg.authorization.core.util.ObjectMapperFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;

import java.time.Duration;

@EnableCaching
@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setDefaultSerializer(Jackson2JsonRedisSerializerHolder.INSTANCE);
        template.setEnableDefaultSerializer(true);

        return template;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return Jackson2JsonRedisSerializerHolder.INSTANCE;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(Jackson2JsonRedisSerializerHolder.INSTANCE))
                        .disableCachingNullValues()
                        .prefixCacheNameWith("athena:")
                        .entryTtl(Duration.ofMinutes(30L))
                )
                .build();
    }

    private static class Jackson2JsonRedisSerializerHolder {
        private static final RedisSerializer<Object> INSTANCE;

        static {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            objectMapper.registerModule(new OAuth2ClientJackson2Module());
            objectMapper.addMixIn(AccountExpiredException.class, AuthenticationExceptionMixIn.class);
            objectMapper.addMixIn(CredentialsExpiredException.class, AuthenticationExceptionMixIn.class);
            objectMapper.addMixIn(DisabledException.class, AuthenticationExceptionMixIn.class);
            objectMapper.addMixIn(LockedException.class, AuthenticationExceptionMixIn.class);
            INSTANCE = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        }
    }
}
