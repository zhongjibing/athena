package com.icezhg.athena.controller.system;

import com.icezhg.athena.domain.Role;
import com.icezhg.athena.service.RoleService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.RoleInfo;
import com.icezhg.athena.vo.RoleQuery;
import com.icezhg.commons.exception.ErrorCodeException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public Role add(@Validated @RequestBody RoleInfo role) {
        if (!roleService.checkUnique(role)) {
            throw new ErrorCodeException("role name is already exists");
        }
        return roleService.save(role);
    }

    @GetMapping("/list")
    public PageResult list(RoleQuery query) {
        return new PageResult(roleService.count(query), roleService.find(query));
    }
}
