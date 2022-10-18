package com.icezhg.athena.service.monitor.impl;

import com.icezhg.athena.constant.CacheKey;
import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.service.monitor.OnlineUserService;
import com.icezhg.athena.vo.OnlineUser;
import com.icezhg.athena.vo.query.OnlineUserQuery;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.commons.util.RefOptional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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

        List<String> keyList = keys.stream().map(key -> (String) key).toList();
        List<OnlineUser> onlineUsers = new ArrayList<>(sessions.size());
        for (int i = 0; i < sessions.size(); i++) {
            Object session = sessions.get(i);
            if (session instanceof Map<?, ?> detail) {
                OnlineUser onlineUser = buildOnlineUser(detail);
                String sessionId = StringUtils.substringAfterLast(keyList.get(i), ":");
                onlineUser.setSessionId(sessionId);

                onlineUsers.add(onlineUser);
            }
        }

        onlineUsers.sort((o1, o2) -> (int) (parseTime(o2.getLoginTime()) - parseTime(o1.getLoginTime())));

        return onlineUsers;
    }

    private OnlineUser buildOnlineUser(Map<?, ?> detail) {
        OnlineUser.OnlineUserBuilder builder = OnlineUser.builder();
        detail.forEach((key, value) -> {
            if (Objects.equals(key, "creationTime")) {
                builder.loginTime(formatDateTime((Long) value));
            } else if (Objects.equals(key, "sessionAttr:SPRING_SECURITY_CONTEXT")) {
                RefOptional.of(value)
                        .cast(SecurityContext.class).map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
                        .cast(DefaultOidcUser.class).map(DefaultOidcUser::getUserInfo)
                        .ifPresent(userInfo -> buildBuilder(builder, userInfo));
            } else if (Objects.equals(key, "lastAccessedTime")) {
                builder.lastAccessedTime(formatDateTime((Long) value));
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

        List<OnlineUser> filtered = stream.toList();
        int total = filtered.size();
        List<OnlineUser> result = filtered.stream().skip(query.getOffset()).limit(query.getPageSize()).toList();

        return new PageResult(total, result);
    }

    private String formatDateTime(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        try {
            long millis = Long.parseLong(text);
            return formatDateTime(millis);
        } catch (NumberFormatException ignored) {
        }

        return "";
    }

    private String formatDateTime(Long millis) {
        if (millis == null) {
            return "";
        }

        return DateFormatUtils.format(millis, Constants.DEFAULT_DATETIME_FORMAT_PATTERN, Constants.DEFAULT_LOCALE);
    }

    private long parseTime(String datetime) {
        try {
            return DateUtils.parseDate(datetime, Constants.DEFAULT_LOCALE, Constants.DEFAULT_DATETIME_FORMAT_PATTERN).getTime();
        } catch (ParseException ignored) {
        }
        return 0L;
    }
}
