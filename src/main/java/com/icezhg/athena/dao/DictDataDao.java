package com.icezhg.athena.dao;

import com.icezhg.athena.domain.DictData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/08.
 */
@Repository
public interface DictDataDao {

    int deleteById(Integer id);

    int insert(DictData record);

    int update(DictData record);

    List<DictData> findByType(String dictType);
}
