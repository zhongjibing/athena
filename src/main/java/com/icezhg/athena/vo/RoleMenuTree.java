package com.icezhg.athena.vo;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/09.
 */
public record RoleMenuTree(
        List<Integer> checkedKeys,
        List<MenuTree> menus
) {}
