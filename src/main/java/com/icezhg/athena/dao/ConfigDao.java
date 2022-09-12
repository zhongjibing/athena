package com.icezhg.athena.dao;

import com.icezhg.athena.domain.Config;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongjibing on 2021/01/11
 */
@Repository
public interface ConfigDao {

    int insert(Config record);

    int update(Config record);

    int deleteById(Integer id);

    Config findById(Integer id);

    Config findByKey(String key);

    int count(Map<String, Object> query);

    List<Config> find(Map<String, Object> query);
}
