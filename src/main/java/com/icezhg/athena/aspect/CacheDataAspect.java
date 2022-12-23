package com.icezhg.athena.aspect;

import com.icezhg.athena.constant.CacheKey;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by zhongjibing on 2022/12/24.
 */
@Aspect
@Component
public class CacheDataAspect {

    private final StringRedisTemplate redisTemplate;

    public CacheDataAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("execution(* com.icezhg.athena.service.common.IpLocationService.search(String)) && args(ip)")
    public Object aroundSearchIpLocation(ProceedingJoinPoint pjp, String ip) throws Throwable {
        if (StringUtils.isNotBlank(ip)) {
            Object result = redisTemplate.opsForHash().get(CacheKey.IP_LOCATIONS, ip);
            if (result == null) {
                result = pjp.proceed();
                if (result != null) {
                    redisTemplate.opsForHash().put(CacheKey.IP_LOCATIONS, ip, result);
                }
            }
            return result;
        }
        return pjp.proceed();
    }


}
