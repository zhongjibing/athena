package com.icezhg.athena.service.system;

import com.icezhg.athena.vo.MenuInfo;
import com.icezhg.athena.vo.MenuQuery;
import com.icezhg.athena.vo.MenuTree;
import com.icezhg.athena.vo.RoleMenuTree;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/07.
 */
public interface MenuService {

    MenuInfo save(MenuInfo menuInfo);

    MenuInfo update(MenuInfo menuInfo);

    List<MenuInfo> listRoleMenus();

    List<MenuTree> buildMenuTreeSelect();

    List<MenuTree> buildMenuTree(List<MenuInfo> menus);

    RoleMenuTree buildRoleMenuTreeSelect(Integer roleId);

    List<MenuInfo> list(MenuQuery query);

    MenuInfo findMenu(Integer menuId);

    int delete(Integer menuId);
}
