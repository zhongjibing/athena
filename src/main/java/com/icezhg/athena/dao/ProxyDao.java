package com.icezhg.athena.dao;


import com.icezhg.athena.domain.Proxy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongjibing on 2023/09/20.
 */
@Repository
public interface ProxyDao {

    int insert(Proxy record);

    int update(Proxy record);

    int count(Map<String, Object> query);

    List<Proxy> find(Map<String, Object> query);

    Proxy findByIpAndPort(@Param("ip") String ip, @Param("port") int port);

    int deleteUnavailable();
}
