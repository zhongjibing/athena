package com.icezhg.athena.service;

import com.icezhg.athena.dao.RoleDao;
import com.icezhg.athena.domain.Role;
import com.icezhg.athena.vo.RoleQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhongjibing on 2020/03/18
 */
@Service
public class RoleService {

    private final RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public int count(RoleQuery query) {
        return roleDao.count(query.toMap());
    }

    public List<Role> find(RoleQuery query) {
        return roleDao.find(query.toMap());
    }
}
