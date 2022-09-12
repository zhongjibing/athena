package com.icezhg.athena.dao;

import com.icezhg.athena.domain.BinaryData;
import org.springframework.stereotype.Repository;

/**
 * Created by zhongjibing on 2022/09/10.
 */
@Repository
public interface BinaryDataDao {

    int insert(BinaryData record);

    BinaryData findByMd5(String md5);
}
