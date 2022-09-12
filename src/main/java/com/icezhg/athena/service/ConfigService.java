package com.icezhg.athena.service;

import com.icezhg.athena.dao.ConfigDao;
import com.icezhg.athena.domain.Config;
import com.icezhg.athena.vo.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/13.
 */
@Service
public class ConfigService {

    private final ConfigDao configDao;

    public ConfigService(ConfigDao configDao) {
        this.configDao = configDao;
    }

    public int count(Query query) {
        return configDao.count(query.toMap());
    }

    public List<Config> find(Query query) {
        return configDao.find(query.toMap());
    }

    public String findConfig(String key) {
        Config config = configDao.findByKey(key);
        return config != null ? config.getValue() : null;
    }
}
