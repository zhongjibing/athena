package com.icezhg.athena.service.system.impl;

import com.icezhg.athena.dao.RoleDao;
import com.icezhg.athena.dao.UserDao;
import com.icezhg.athena.dao.UserRoleDao;
import com.icezhg.athena.domain.Role;
import com.icezhg.athena.service.system.UserRoleService;
import com.icezhg.athena.util.MaskSensitiveUtil;
import com.icezhg.athena.vo.NameQuery;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.Query;
import com.icezhg.athena.vo.RoleAuth;
import com.icezhg.athena.vo.UserAuth;
import com.icezhg.athena.vo.UserInfo;
import com.icezhg.athena.vo.UserQuery;
import com.icezhg.commons.exception.ParameterException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;

    public UserRoleServiceImpl(UserDao userDao, RoleDao roleDao, UserRoleDao userRoleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
    }

    @Override
    public UserAuth findAuth(Long userId) {
        UserAuth userAuth = new UserAuth();
        UserInfo userInfo = userDao.findById(userId);
        if (userInfo != null) {
            userAuth.setId(userInfo.getId());
            userAuth.setUsername(userInfo.getUsername());
            userAuth.setNickname(userInfo.getNickname());
        }

        Set<Integer> grantedRoles = roleDao.findAuthRoles(userId).stream().map(Role::getId).collect(Collectors.toSet());
        List<RoleAuth> roleAuths = roleDao.listAll().stream().map(role -> {
            RoleAuth roleAuth = new RoleAuth();
            roleAuth.setId(role.getId());
            roleAuth.setName(role.getName());
            roleAuth.setRoleKey(role.getRoleKey());
            roleAuth.setRoleSort(role.getRoleSort());
            roleAuth.setCreateTime(role.getCreateTime());
            roleAuth.setRemark(role.getRemark());
            roleAuth.setGranted(grantedRoles.contains(role.getId()));
            return roleAuth;
        }).toList();
        userAuth.setRoleAuths(roleAuths);
        return userAuth;
    }

    @Override
    @Transactional
    public UserAuth updateUserAuth(Long userId, List<Integer> roleIds) {
        Set<Integer> delRoles = roleDao.findAuthRoles(userId).stream().map(Role::getId).collect(Collectors.toSet());
        Set<Integer> addRoles = new HashSet<>(roleIds);
        Iterator<Integer> iterator = addRoles.iterator();
        while (iterator.hasNext()) {
            Integer roleId = iterator.next();
            if (delRoles.contains(roleId)) {
                delRoles.remove(roleId);
                iterator.remove();
            }
        }

        if (!addRoles.isEmpty()) {
            userRoleDao.addUserRoles(userId, addRoles);
        }
        if (!delRoles.isEmpty()) {
            userRoleDao.deleteUserRoles(userId, delRoles);
        }

        return findAuth(userId);
    }

    @Override
    public PageResult listAllocatedUsers(Integer roleId, NameQuery query) {
        UserQuery userQuery = new UserQuery();
        userQuery.setRoleId(roleId);
        userQuery.setName(query.getName());
        userQuery.setPageNum(query.getPageNum());
        userQuery.setPageSize(query.getPageSize());
        return getUserInfoPageResult(userQuery);
    }

    @Override
    public PageResult listUnallocatedUsers(Integer roleId, NameQuery query) {
        UserQuery userQuery = new UserQuery();
        userQuery.setNonRoleId(roleId);
        userQuery.setName(query.getName());
        userQuery.setPageNum(query.getPageNum());
        userQuery.setPageSize(query.getPageSize());
        return getUserInfoPageResult(userQuery);
    }

    private PageResult getUserInfoPageResult(Query query) {
        Map<String, Object> queryParam = query.toMap();
        int total = userDao.count(queryParam);
        List<UserInfo> userInfos = userDao.find(queryParam);
        userInfos.forEach(user -> user.setMobile(MaskSensitiveUtil.maskMobile(user.getMobile())));
        return new PageResult(total, userInfos);
    }

    @Override
    public void authUser(Integer roleId, List<Long> userIds) {
        if (roleId == null || userIds == null || userIds.isEmpty()) {
            throw new ParameterException("", "parameter error");
        }

        List<Long> actual = userIds.stream().filter(item -> item != null && item > 0L).collect(Collectors.toList());
        userRoleDao.addRoleUsers(roleId, actual);
    }

    @Override
    public void cancelAuth(Integer roleId, List<Long> userIds) {
        if (roleId == null || userIds == null || userIds.isEmpty()) {
            throw new ParameterException("", "parameter error");
        }

        userRoleDao.deleteRoleUsers(roleId, userIds);
    }
}
