package com.icezhg.athena.dao;

import com.icezhg.athena.domain.DictType;
import org.springframework.stereotype.Repository;

/**
 * Created by zhongjibing on 2022/09/08.
 */
@Repository
public interface DictTypeDao {

    int deleteById(Long id);

    int insert(DictType record);

    DictType findById(Long id);

    int update(DictType record);
}
