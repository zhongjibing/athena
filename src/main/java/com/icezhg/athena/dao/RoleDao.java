package com.icezhg.athena.dao;

import com.icezhg.athena.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongjibing on 2020/03/18
 */
@Repository
public interface RoleDao {

    int insert(Role role);

    int update(Role role);

    int deleteById(Integer id);

    Role findById(Integer id);

    Role findByName(String name);

    int count(Map<String, Object> params);
    List<Role> find(Map<String, Object> params);

    List<Role> findByUserId(Integer userId);

    List<String> findRoleNameByUserId(Integer userId);

    List<String> findRoleNameByResourceName(String name);
}
