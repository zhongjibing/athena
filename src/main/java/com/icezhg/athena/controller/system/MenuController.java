package com.icezhg.athena.controller.system;

import com.icezhg.athena.domain.Menu;
import com.icezhg.athena.service.MenuService;
import com.icezhg.athena.vo.MenuTree;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/tree")
    public List<MenuTree> menuTree() {
        return menuService.buildMenuTreeSelect();
    }
}
