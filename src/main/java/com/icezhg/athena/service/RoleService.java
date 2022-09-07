package com.icezhg.athena.service;

import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.dao.RoleDao;
import com.icezhg.athena.dao.RoleMenuDao;
import com.icezhg.athena.domain.Role;
import com.icezhg.athena.domain.RoleMenu;
import com.icezhg.athena.vo.RoleInfo;
import com.icezhg.athena.vo.RoleQuery;
import com.icezhg.authorization.core.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhongjibing on 2020/03/18
 */
@Service
public class RoleService {

    private final RoleDao roleDao;

    private final RoleMenuDao roleMenuDao;

    public RoleService(RoleDao roleDao, RoleMenuDao roleMenuDao) {
        this.roleDao = roleDao;
        this.roleMenuDao = roleMenuDao;
    }

    public int count(RoleQuery query) {
        return roleDao.count(query.toMap());
    }

    public List<Role> find(RoleQuery query) {
        return roleDao.find(query.toMap());
    }

    public List<Role> findCurrentRole() {
        return roleDao.findCurrentRole(SecurityUtil.currentUserId());
    }

    public boolean checkUnique(RoleInfo role) {
        RoleQuery query = new RoleQuery();
        query.setName(role.getName());
        return find(query).isEmpty();
    }

    public Role save(RoleInfo role) {
        Role newRole = buildRole(role);
        newRole.setCreateTime(new Date());
        newRole.setCreateBy(SecurityUtil.currentUserName());
        newRole.setUpdateTime(new Date());
        newRole.setUpdateBy(SecurityUtil.currentUserName());
        roleDao.insert(newRole);
        if (!CollectionUtils.isEmpty(role.getMenuIds())) {
            roleMenuDao.batchInsert(buildRoleMenus(newRole.getId(), role.getMenuIds()));
        }
        return newRole;
    }

    private List<RoleMenu> buildRoleMenus(Integer roleId, List<Integer> menuIds) {
        return menuIds.stream().map(menuId -> new RoleMenu(roleId, menuId)).collect(Collectors.toList());
    }

    private Role buildRole(RoleInfo role) {
        Role newRole = new Role();
        newRole.setName(role.getName());
        newRole.setDescription(role.getDescription());
        newRole.setOrderNum(role.getOrderNum());
        newRole.setDataScope(role.getDataScope());
        newRole.setMenuCheckStrictly(role.isMenuCheckStrictly()? Constants.YES: Constants.NO);
        newRole.setDeptCheckStrictly(role.isDeptCheckStrictly()? Constants.YES: Constants.NO);
        newRole.setStatus(role.getStatus());
        newRole.setRemark(role.getRemark());
        return newRole;
    }
}
