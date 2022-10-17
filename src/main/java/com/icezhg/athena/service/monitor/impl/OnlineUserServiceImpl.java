package com.icezhg.athena.service.monitor.impl;

import com.icezhg.athena.constant.CacheKey;
import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.service.monitor.OnlineUserService;
import com.icezhg.athena.vo.OnlineUser;
import com.icezhg.athena.vo.OnlineUserQuery;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.commons.util.RefOptional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {

    private final RedisTemplate<Object, Object> redisTemplate;

    public OnlineUserServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PageResult listOnlineUsers(OnlineUserQuery query) {
        Object cache = redisTemplate.opsForValue().get(CacheKey.ONLINE_USERS);
        if (cache != null) {
            @SuppressWarnings("unchecked")
            List<OnlineUser> onlineUsers = (List<OnlineUser>) cache;
            return buildPageResult(onlineUsers, query);
        }

        List<OnlineUser> onlineUsers = listOnlineUsers();
        redisTemplate.opsForValue().set(CacheKey.ONLINE_USERS, onlineUsers, Duration.ofMinutes(1L));
        return buildPageResult(onlineUsers, query);
    }

    private List<OnlineUser> listOnlineUsers() {
        Set<Object> keys = redisTemplate.keys(CacheKey.SESSION_KEYS_PATTERN);
        if (keys == null) {
            return Collections.emptyList();
        }

        List<Object> sessions = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            RedisSerializer<String> serializer = RedisSerializer.string();
            keys.stream().map(key -> serializer.serialize((String) key)).filter(Objects::nonNull).forEach(connection::hGetAll);
            return null;
        });

        List<OnlineUser> onlineUsers = new ArrayList<>(sessions.size());
        for (Object session : sessions) {
            if (session instanceof Map<?, ?> detail) {
                onlineUsers.add(buildOnlineUser(detail));
            }
        }
        return onlineUsers;
    }

    private OnlineUser buildOnlineUser(Map<?, ?> detail) {
        OnlineUser.OnlineUserBuilder builder = OnlineUser.builder();
        detail.forEach((key, value) -> {
            if (Objects.equals(key, "creationTime")) {
                builder.loginTime(formatDateTime((String) value));
            } else if (Objects.equals(key, "sessionAttr:SPRING_SECURITY_CONTEXT")) {
                RefOptional.of(value)
                        .cast(SecurityContext.class).map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
                        .cast(DefaultOidcUser.class).map(DefaultOidcUser::getUserInfo)
                        .ifPresent(userInfo -> buildBuilder(builder, userInfo));
            } else if (Objects.equals(key, "lastAccessedTime")) {
                builder.lastAccessedTime(formatDateTime((String) value));
            }
        });
        return builder.build();
    }

    private void buildBuilder(OnlineUser.OnlineUserBuilder builder, OidcUserInfo userInfo) {
        builder
                .username(userInfo.getClaimAsString("name"))
                .birthdate(userInfo.getClaimAsString("birthdate"))
                .gender(userInfo.getClaimAsString("gender"))
                .loginIp(userInfo.getClaimAsString("ip"))
                .mobile(userInfo.getClaimAsString("mobile"))
                .picture(userInfo.getClaimAsString("picture"))
                .nickname(userInfo.getClaimAsString("nickname"))
                .id(userInfo.getClaimAsString("id"))
                .email(userInfo.getClaimAsString("email"))
                .createTime(formatDateTime(userInfo.getClaimAsString("ctime")))
                .userAgent(userInfo.getClaimAsString("agent"))
                .aud(userInfo.getClaimAsStringList("aud"));
    }

    private PageResult buildPageResult(List<OnlineUser> onlineUsers, OnlineUserQuery query) {
        Stream<OnlineUser> stream = onlineUsers.stream();
        if (StringUtils.isNotBlank(query.getUsername())) {
            stream = stream.filter(user -> StringUtils.contains(user.getUsername(), query.getUsername()));
        }
        if (StringUtils.isNotBlank(query.getLoginIp())) {
            stream = stream.filter(user -> StringUtils.equals(user.getLoginIp(), query.getLoginIp()));
        }
        int total = (int) stream.count();
        List<OnlineUser> result = stream.skip(query.getOffset()).limit(query.getPageSize()).toList();
        return new PageResult(total, result);
    }

    private String formatDateTime(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        try {
            long millis = Long.parseLong(text);
            return DateFormatUtils.format(millis, Constants.DEFAULT_DATETIME_FORMAT_PATTERN, Constants.DEFAULT_LOCALE);
        } catch (NumberFormatException ignored) {
        }

        return "";
    }
}
