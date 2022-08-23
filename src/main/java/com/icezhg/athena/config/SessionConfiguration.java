package com.icezhg.athena.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by zhongjibing on 2021/01/16
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfiguration {
}
