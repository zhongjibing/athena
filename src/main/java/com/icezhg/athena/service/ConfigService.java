package com.icezhg.athena.service;

import com.icezhg.athena.dao.ConfigDao;
import com.icezhg.athena.domain.Config;
import com.icezhg.athena.vo.ConfigInfo;
import com.icezhg.athena.vo.ConfigQuery;
import com.icezhg.athena.vo.Query;
import com.icezhg.authorization.core.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public boolean checkUnique(ConfigInfo configInfo) {
        ConfigQuery query = new ConfigQuery();
        query.setFuzzyQuery(false);
        query.setKey(configInfo.getKey());
        List<Config> configs = find(query);
        return configs.isEmpty() || Objects.equals(configInfo.getId(), configs.get(0).getId());
    }

    public ConfigInfo save(ConfigInfo configInfo) {
        Config config = buildConfig(configInfo);
        config.setCreateBy(SecurityUtil.currentUserName());
        config.setCreateTime(new Date());
        config.setUpdateBy(SecurityUtil.currentUserName());
        config.setUpdateTime(new Date());
        configDao.insert(config);
        configInfo.setId(config.getId());
        return configInfo;
    }

    public ConfigInfo update(ConfigInfo configInfo) {
        Config config = buildConfig(configInfo);
        config.setUpdateBy(SecurityUtil.currentUserName());
        config.setUpdateTime(new Date());
        configDao.update(config);
        return findById(configInfo.getId());
    }

    public int deleteByIds(List<Integer> ids) {
        return configDao.deleteByIds(ids);
    }

    public ConfigInfo findById(Integer id) {
        return buildConfigInfo(configDao.findById(id));
    }

    private Config buildConfig(ConfigInfo configInfo) {
        Config config = new Config();
        config.setId(configInfo.getId());
        config.setName(configInfo.getName());
        config.setKey(configInfo.getKey());
        config.setValue(configInfo.getValue());
        config.setType(configInfo.getType());
        config.setRemark(configInfo.getRemark());
        return config;
    }

    private ConfigInfo buildConfigInfo(Config config) {
        ConfigInfo configInfo = new ConfigInfo();
        if (config != null) {
            configInfo.setId(config.getId());
            configInfo.setName(config.getName());
            configInfo.setKey(config.getKey());
            configInfo.setValue(config.getValue());
            configInfo.setType(config.getType());
            configInfo.setRemark(config.getRemark());
        }
        return configInfo;
    }
}
