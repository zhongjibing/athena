package com.icezhg.athena.service.impl;

import com.icezhg.athena.dao.ConfigDao;
import com.icezhg.athena.domain.Config;
import com.icezhg.athena.service.ConfigService;
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
public class ConfigServiceImpl implements ConfigService {

    private final ConfigDao configDao;

    public ConfigServiceImpl(ConfigDao configDao) {
        this.configDao = configDao;
    }

    @Override
    public int count(Query query) {
        return configDao.count(query.toMap());
    }

    @Override
    public List<Config> find(Query query) {
        return configDao.find(query.toMap());
    }

    @Override
    public String findConfig(String key) {
        Config config = configDao.findByKey(key);
        return config != null ? config.getValue() : null;
    }

    @Override
    public boolean checkUnique(ConfigInfo configInfo) {
        ConfigQuery query = new ConfigQuery();
        query.setFuzzyQuery(false);
        query.setKey(configInfo.getKey());
        List<Config> configs = find(query);
        return configs.isEmpty() || Objects.equals(configInfo.getId(), configs.get(0).getId());
    }

    @Override
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

    @Override
    public ConfigInfo update(ConfigInfo configInfo) {
        Config config = buildConfig(configInfo);
        config.setUpdateBy(SecurityUtil.currentUserName());
        config.setUpdateTime(new Date());
        configDao.update(config);
        return findById(configInfo.getId());
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return configDao.deleteByIds(ids);
    }

    @Override
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
