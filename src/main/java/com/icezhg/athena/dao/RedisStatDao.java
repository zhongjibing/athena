package com.icezhg.athena.dao;

import com.icezhg.athena.domain.RedisStat;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface RedisStatDao {

    int insert(RedisStat record);

    int batchInsert(RedisStat record);

    int count(Map<String, Object> query);

    List<RedisStat> find(Map<String, Object> query);

}
