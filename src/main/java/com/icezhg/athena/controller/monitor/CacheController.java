package com.icezhg.athena.controller.monitor;


import com.icezhg.athena.service.monitor.CacheService;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/monitor/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping
    public Object getInfo() {
        return cacheService.getCacheInfo();
    }
}
