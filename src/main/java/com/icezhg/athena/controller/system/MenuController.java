package com.icezhg.athena.controller.system;

import com.icezhg.athena.service.system.MenuService;
import com.icezhg.athena.vo.MenuInfo;
import com.icezhg.athena.vo.query.MenuQuery;
import com.icezhg.athena.vo.MenuTree;
import com.icezhg.athena.vo.RoleMenuTree;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{menuId}")
    public MenuInfo get(@PathVariable Integer menuId) {
        return menuService.findMenu(menuId);
    }

    @DeleteMapping("/{menuId}")
    public int delete(@PathVariable Integer menuId) {
        return menuService.delete(menuId);
    }

    @PostMapping
    public MenuInfo add(@RequestBody MenuInfo menuInfo) {
        return menuService.save(menuInfo);
    }

    @PutMapping
    public MenuInfo edit(@RequestBody MenuInfo menuInfo) {
        return menuService.update(menuInfo);
    }


    @GetMapping("/tree")
    public List<MenuTree> roleFilteredMenuTree() {
        return menuService.buildMenuTreeSelect();
    }

    @GetMapping("/roleMenuTree/{roleId}")
    public RoleMenuTree roleMenuTree(@PathVariable Integer roleId) {
        return menuService.buildRoleMenuTreeSelect(roleId);
    }

    @GetMapping("/list")
    public Object list(MenuQuery query) {
        return menuService.list(query);
    }
}
