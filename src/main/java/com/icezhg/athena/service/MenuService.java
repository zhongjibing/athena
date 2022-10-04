package com.icezhg.athena.service;

import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.dao.MenuDao;
import com.icezhg.athena.dao.RoleDao;
import com.icezhg.athena.dao.RoleMenuDao;
import com.icezhg.athena.domain.Menu;
import com.icezhg.athena.domain.Role;
import com.icezhg.athena.vo.MenuInfo;
import com.icezhg.athena.vo.MenuQuery;
import com.icezhg.athena.vo.MenuTree;
import com.icezhg.athena.vo.RoleMenuTree;
import com.icezhg.authorization.core.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public MenuInfo update(MenuInfo menuInfo) {
        Menu menu = menuInfo.toMenu();
        menu.setUpdateBy(SecurityUtil.currentUserName());
        menu.setUpdateTime(new Date());
        menuDao.update(menu);
        return menuInfo;
    }

    public List<MenuInfo> listRoleMenus() {
        List<Role> roles = roleDao.findAuthRoles(SecurityUtil.currentUserId());
        MenuQuery query = new MenuQuery();
        query.setStatus(Constants.NORMAL);
        if (!hasRootRole(roles)) {
            query.setUserId(SecurityUtil.currentUserId());
        }
        return menuDao.list(query.toMap());
    }

    public List<MenuTree> buildMenuTreeSelect() {
        return buildMenuTree(listRoleMenus());
    }

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

    public List<MenuInfo> list(MenuQuery query) {
        return menuDao.list(query.toMap());
    }

    public MenuInfo findMenu(Integer menuId) {
        return menuDao.findById(menuId);
    }

    public int delete(Integer menuId) {
        return menuDao.deleteById(menuId);
    }
}
