package com.icezhg.athena.service;

import com.icezhg.athena.dao.MenuDao;
import com.icezhg.athena.dao.RoleDao;
import com.icezhg.athena.dao.RoleMenuDao;
import com.icezhg.athena.domain.Menu;
import com.icezhg.athena.domain.Role;
import com.icezhg.athena.vo.MenuTree;
import com.icezhg.athena.vo.RoleMenuTree;
import com.icezhg.authorization.core.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Service
public class MenuService {

    private final MenuDao menuDao;

    private final RoleDao roleDao;

    private final RoleMenuDao roleMenuDao;

    public MenuService(MenuDao menuDao, RoleDao roleDao, RoleMenuDao roleMenuDao) {
        this.menuDao = menuDao;
        this.roleDao = roleDao;
        this.roleMenuDao = roleMenuDao;
    }

    public List<Menu> list() {
        List<Role> roles = roleDao.findCurrentRole(SecurityUtil.currentUserId());
        if (hasRootRole(roles)) {
            return menuDao.listAll();
        }
        return menuDao.findByUserId(SecurityUtil.currentUserId());
    }

    public List<MenuTree> buildMenuTreeSelect() {
        return buildMenuTree(list());
    }

    public List<MenuTree> buildMenuTree(List<Menu> menus) {
        Set<Integer> menuIds = new HashSet<>();
        for (Menu menu : menus) {
            menuIds.add(menu.getId());
        }

        List<MenuTree> returnList = new ArrayList<>();
        for (Menu menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!menuIds.contains(menu.getParentId())) {
                MenuTree treeNode = recursionFn(menus, menu);
                returnList.add(treeNode);
            }
        }
        return returnList;
    }

    private MenuTree recursionFn(List<Menu> menus, Menu menu) {
        MenuTree treeNode = new MenuTree();
        treeNode.setId(menu.getId());
        treeNode.setLabel(menu.getName());
        treeNode.setMenu(menu);
        List<MenuTree> children = new LinkedList<>();
        List<Menu> childList = getChildList(menus, menu);
        for (Menu child : childList) {
            children.add(recursionFn(menus, child));
        }
        treeNode.setChildren(children);
        return treeNode;
    }

    private List<Menu> getChildList(List<Menu> menus, Menu menu) {
        List<Menu> tlist = new ArrayList<>();
        for (Menu item : menus) {
            if (item.getParentId().intValue() == menu.getId().intValue()) {
                tlist.add(item);
            }
        }
        return tlist;
    }

    private boolean hasRootRole(List<Role> roles) {
        for (Role role : roles) {
            if (role.isRoot()) {
                return true;
            }
        }
        return false;
    }

    public RoleMenuTree buildRoleMenuTreeSelect(Integer roleId) {
        return new RoleMenuTree(roleMenuDao.findMenuIdsByRoleId(roleId), buildMenuTreeSelect());
    }
}
