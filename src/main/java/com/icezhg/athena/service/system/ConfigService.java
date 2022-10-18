package com.icezhg.athena.service.system;

import com.icezhg.athena.domain.Config;
import com.icezhg.athena.vo.ConfigInfo;
import com.icezhg.athena.vo.query.Query;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/13.
 */
public interface ConfigService {

    int count(Query query);

    List<Config> find(Query query);

    String findConfig(String key);

    boolean checkUnique(ConfigInfo configInfo);

    ConfigInfo save(ConfigInfo configInfo);

    ConfigInfo update(ConfigInfo configInfo);

    int deleteByIds(List<Integer> ids);

    ConfigInfo findById(Integer id);

}
