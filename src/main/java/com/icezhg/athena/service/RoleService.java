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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    public Role findById(Integer roleId) {
        RoleQuery query = new RoleQuery();
        query.setId(roleId);
        return find(query).stream().findFirst().orElse(null);
    }

    public RoleInfo findRoleInfo(Integer roleId) {
        return buildRoleInfo(findById(roleId));
    }

    public List<Role> findCurrentRole() {
        return roleDao.findCurrentRole(SecurityUtil.currentUserId());
    }

    public boolean checkUnique(RoleInfo role) {
        RoleQuery query = new RoleQuery();
        query.setName(role.getName());
        List<Role> roles = find(query);
        return roles.isEmpty() || Objects.equals(role.getId(), roles.get(0).getId());
    }

    @Transactional
    public RoleInfo save(RoleInfo role) {
        Role newRole = buildRole(role);
        newRole.setCreateTime(new Date());
        newRole.setCreateBy(SecurityUtil.currentUserName());
        newRole.setUpdateTime(new Date());
        newRole.setUpdateBy(SecurityUtil.currentUserName());
        roleDao.insert(newRole);
        if (!CollectionUtils.isEmpty(role.getMenuIds())) {
            roleMenuDao.batchInsert(buildRoleMenus(newRole.getId(), role.getMenuIds()));
        }
        role.setId(newRole.getId());
        return role;
    }

    @Transactional
    public RoleInfo update(RoleInfo role) {
        Role existing = buildRole(role);
        existing.setUpdateTime(new Date());
        existing.setUpdateBy(SecurityUtil.currentUserName());
        roleDao.update(existing);
        roleMenuDao.deleteByRoleIds(List.of(role.getId()));
        if (!CollectionUtils.isEmpty(role.getMenuIds())) {
            roleMenuDao.batchInsert(buildRoleMenus(existing.getId(), role.getMenuIds()));
        }

        return role;
    }

    private List<RoleMenu> buildRoleMenus(Integer roleId, List<Integer> menuIds) {
        return menuIds.stream().map(menuId -> new RoleMenu(roleId, menuId)).collect(Collectors.toList());
    }

    private Role buildRole(RoleInfo role) {
        Role newRole = new Role();
        newRole.setId(role.getId());
        newRole.setName(role.getName());
        newRole.setDescription(role.getDescription());
        newRole.setOrderNum(role.getOrderNum());
        newRole.setDataScope(role.getDataScope());
        newRole.setMenuCheckStrictly(role.isMenuCheckStrictly() ? Constants.YES : Constants.NO);
        newRole.setDeptCheckStrictly(role.isDeptCheckStrictly() ? Constants.YES : Constants.NO);
        newRole.setStatus(role.getStatus());
        newRole.setRemark(role.getRemark());
        return newRole;
    }

    private RoleInfo buildRoleInfo(Role role) {
        RoleInfo roleInfo = new RoleInfo();
        if (role != null) {
            roleInfo.setId(role.getId());
            roleInfo.setName(role.getName());
            roleInfo.setDescription(role.getDescription());
            roleInfo.setOrderNum(role.getOrderNum());
            roleInfo.setDataScope(role.getDataScope());
            roleInfo.setMenuCheckStrictly(role.getMenuCheckStrictly() == Constants.YES);
            roleInfo.setDeptCheckStrictly(role.getDeptCheckStrictly() == Constants.YES);
            roleInfo.setStatus(role.getStatus());
            roleInfo.setRemark(role.getRemark());
        }
        return roleInfo;
    }

    @Transactional
    public int deleteRoles(List<Integer> roleIds) {
        roleMenuDao.deleteByRoleIds(roleIds);
        return roleDao.deleteByIds(roleIds);
    }
}
