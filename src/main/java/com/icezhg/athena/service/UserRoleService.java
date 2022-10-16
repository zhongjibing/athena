package com.icezhg.athena.service;

import com.icezhg.athena.vo.NameQuery;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.UserAuth;

import java.util.List;

public interface UserRoleService {

    UserAuth findAuth(Long userId);

    UserAuth updateUserAuth(Long userId, List<Integer> roleIds);

    PageResult listAllocatedUsers(Integer roleId, NameQuery query);

    PageResult listUnallocatedUsers(Integer roleId, NameQuery query);

    void authUser(Integer roleId, List<Long> userIds);

    void cancelAuth(Integer roleId, List<Long> userIds);
}
