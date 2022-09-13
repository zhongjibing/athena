package com.icezhg.athena.dao;

import com.icezhg.athena.domain.User;
import com.icezhg.athena.vo.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Repository
public interface UserDao {

    int insert(User user);

    int update(User user);

    int count(Map<String, Object> query);

    List<UserInfo> find(Map<String, Object> query);

    UserInfo findById(Long id);

    String findUserPasswd(Long userId);
}
