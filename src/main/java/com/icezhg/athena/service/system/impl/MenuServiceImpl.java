package com.icezhg.athena.service.system.impl;

import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.dao.MenuDao;
import com.icezhg.athena.dao.RoleDao;
import com.icezhg.athena.dao.RoleMenuDao;
import com.icezhg.athena.domain.Menu;
import com.icezhg.athena.domain.Role;
import com.icezhg.athena.domain.User;
import com.icezhg.athena.service.system.MenuService;
import com.icezhg.athena.vo.MenuInfo;
import com.icezhg.athena.vo.query.MenuQuery;
import com.icezhg.athena.vo.MenuTree;
import com.icezhg.athena.vo.RoleMenuTree;
import com.icezhg.authorization.core.SecurityUtil;
import com.icezhg.commons.exception.InvalidDataStateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Service
public class MenuServiceImpl implements MenuService {

    private final MenuDao menuDao;

    private final RoleDao roleDao;

    private final RoleMenuDao roleMenuDao;

    public MenuServiceImpl(MenuDao menuDao, RoleDao roleDao, RoleMenuDao roleMenuDao) {
        this.menuDao = menuDao;
        this.roleDao = roleDao;
        this.roleMenuDao = roleMenuDao;
    }

    @Override
    public MenuInfo save(MenuInfo menuInfo) {
        Menu menu = menuInfo.toMenu();
        menu.setCreateBy(SecurityUtil.currentUserName());
        menu.setCreateTime(new Date());
        menu.setUpdateBy(SecurityUtil.currentUserName());
        menu.setUpdateTime(new Date());
        menuDao.insert(menu);
        menuInfo.setId(menu.getId());
        return menuInfo;
    }

    @Override
    public MenuInfo update(MenuInfo menuInfo) {
        Menu menu = menuInfo.toMenu();
        menu.setUpdateBy(SecurityUtil.currentUserName());
        menu.setUpdateTime(new Date());
        menuDao.update(menu);
        return menuInfo;
    }

    @Override
    public List<MenuInfo> listRoleMenus() {
        MenuQuery query = new MenuQuery();
        query.setStatus(Constants.NORMAL);

        Long userId = SecurityUtil.currentUserId();
        if (!isAdminRole(userId)) {
            query.setUserId(userId);
        }

        return menuDao.list(query.toMap());
    }

    private boolean isAdminRole(Long userId) {
        if (User.isRoot(userId)) {
            return true;
        }
        List<Role> roles = roleDao.findAuthRoles(userId);
        for (Role role : roles) {
            if (role.isAdmin()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<MenuTree> buildMenuTreeSelect() {
        return buildMenuTree(listRoleMenus());
    }

    @Override
    public List<MenuTree> buildMenuTree(List<MenuInfo> menus) {
        Set<Integer> menuIds = new HashSet<>();
        for (MenuInfo menu : menus) {
            menuIds.add(menu.getId());
        }

        List<MenuTree> returnList = new ArrayList<>();
        for (MenuInfo menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!menuIds.contains(menu.getParentId())) {
                MenuTree treeNode = recursionFn(menus, menu);
                returnList.add(treeNode);
            }
        }
        return returnList;
    }

    private MenuTree recursionFn(List<MenuInfo> menus, MenuInfo menu) {
        MenuTree treeNode = new MenuTree();
        treeNode.setId(menu.getId());
        treeNode.setLabel(menu.getName());
        treeNode.setMenu(menu);
        List<MenuTree> children = new LinkedList<>();
        List<MenuInfo> childList = getChildList(menus, menu);
        for (MenuInfo child : childList) {
            children.add(recursionFn(menus, child));
        }
        children.sort(Comparator.comparingInt(menuTree -> menuTree.getMenu().getOrderNum()));
        treeNode.setChildren(children);
        return treeNode;
    }

    private List<MenuInfo> getChildList(List<MenuInfo> menus, MenuInfo menu) {
        List<MenuInfo> tlist = new ArrayList<>();
        for (MenuInfo item : menus) {
            if (item.getParentId().intValue() == menu.getId().intValue()) {
                tlist.add(item);
            }
        }
        return tlist;
    }

    @Override
    public RoleMenuTree buildRoleMenuTreeSelect(Integer roleId) {
        return new RoleMenuTree(roleMenuDao.findMenuIdsByRoleId(roleId), buildMenuTreeSelect());
    }

    @Override
    public List<MenuInfo> list(MenuQuery query) {
        return menuDao.list(query.toMap());
    }

    @Override
    public MenuInfo findMenu(Integer menuId) {
        return menuDao.findById(menuId);
    }

    @Override
    public int delete(Integer menuId) {
        boolean hasChild = menuDao.hasChildren(menuId);
        if (hasChild) {
            throw new InvalidDataStateException("", "存在子菜单,不允许删除");
        }
        boolean hasRole = roleMenuDao.checkMenuExistRole(menuId);
        if (hasRole) {
            throw new InvalidDataStateException("", "菜单已分配,不允许删除");
        }
        return menuDao.deleteById(menuId);
    }
}
