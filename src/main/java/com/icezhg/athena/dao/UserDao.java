package com.icezhg.athena.dao;

import com.icezhg.athena.domain.User;
import org.springframework.stereotype.Repository;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Repository
public interface UserDao {

    User findByUserId(long id);

    User findByUsername(String username);
}
