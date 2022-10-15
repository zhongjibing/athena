package com.icezhg.athena.service;

import com.icezhg.athena.domain.Role;
import com.icezhg.athena.vo.RoleInfo;
import com.icezhg.athena.vo.RoleQuery;
import com.icezhg.athena.vo.UserQuery;

import java.util.List;

/**
 * Created by zhongjibing on 2020/03/18
 */
public interface RoleService {

    int count(RoleQuery query);

    List<Role> find(RoleQuery query);

    Role findById(Integer roleId);

    RoleInfo findRoleInfo(Integer roleId);

    List<Role> findCurrentRole();

    boolean checkUnique(RoleInfo role);

    RoleInfo save(RoleInfo role);

    RoleInfo update(RoleInfo role);

    int changeStatus(RoleInfo roleInfo);

    int deleteRoles(List<Integer> roleIds);

    List<Role> listAll();

    Object listAllocatedUsers(String roleId, UserQuery query);
}
