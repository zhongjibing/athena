package com.icezhg.athena.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Configuration
@MapperScan("com.icezhg.athena.dao")
public class MybatisConfiguration {
}
