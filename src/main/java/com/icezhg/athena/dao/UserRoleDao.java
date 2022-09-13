package com.icezhg.athena.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by zhongjibing on 2022/09/13.
 */
@Repository
public interface UserRoleDao {

    void addUserRoles(@Param("userId") Long userId, @Param("roleIds") Set<Integer> roleIds);

    void deleteUserRoles(@Param("userId") Long userId, @Param("roleIds") Set<Integer> roleIds);
}
