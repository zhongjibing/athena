package com.icezhg.athena.constant;

public interface CacheKey {
    String SESSION_KEYS_PATTERN = "spring:session:sessions:????????-????-????-????-????????????";

    String ONLINE_USERS = "admin:monitor:online:users";

    String IP_LOCATIONS = "admin:common:ip:locations";
}
