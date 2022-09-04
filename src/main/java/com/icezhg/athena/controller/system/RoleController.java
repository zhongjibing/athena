package com.icezhg.athena.controller.system;

import com.icezhg.athena.service.RoleService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.RoleQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2022/09/04.
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public Object list(RoleQuery query) {
        return new PageResult(roleService.count(query), roleService.find(query));
    }
}
