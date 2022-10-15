package com.icezhg.athena.service;

import com.icezhg.athena.vo.Query;
import com.icezhg.athena.vo.UserAuth;
import com.icezhg.athena.vo.UserInfo;
import com.icezhg.athena.vo.UserPasswd;
import com.icezhg.athena.vo.UserStatus;

import java.util.List;

/**
 * Created by zhongjibing on 2020/03/15
 */
public interface UserService {

    boolean checkUnique(UserInfo userInfo);

    UserInfo save(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);

    int count(Query query);

    List<UserInfo> find(Query query);

    int changeStatus(UserStatus userStatus);

    UserInfo findById(Long userId);

    int resetPasswd(UserPasswd userPasswd);

    UserAuth findAuth(Long userId);

    UserAuth updateUserAuth(Long userId, List<Integer> roleIds);
}
