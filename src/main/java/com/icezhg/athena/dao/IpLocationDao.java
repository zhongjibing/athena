package com.icezhg.athena.dao;

import com.icezhg.athena.domain.IpLocation;
import org.springframework.stereotype.Repository;

/**
 * Created by zhongjibing on 2022/12/24.
 */
@Repository
public interface IpLocationDao {

    IpLocation findByIp(String ip);
}
