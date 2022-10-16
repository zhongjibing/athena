package com.icezhg.athena.controller.system;

import com.icezhg.athena.service.RoleService;
import com.icezhg.athena.service.UserRoleService;
import com.icezhg.athena.service.UserService;
import com.icezhg.athena.vo.NameQuery;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.RoleInfo;
import com.icezhg.athena.vo.RoleQuery;
import com.icezhg.athena.vo.UserQuery;
import com.icezhg.commons.exception.ErrorCodeException;
import org.springframework.validation.annotation.Validated;
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
 * Created by zhongjibing on 2022/09/04.
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    public RoleController(RoleService roleService, UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public RoleInfo add(@Validated @RequestBody RoleInfo role) {
        if (!roleService.checkUnique(role)) {
            throw new ErrorCodeException("", "role name is already exists");
        }
        return roleService.save(role);
    }

    @PutMapping
    public RoleInfo edit(@Validated @RequestBody RoleInfo role) {
        if (!roleService.checkUnique(role)) {
            throw new ErrorCodeException("", "role name is already exists");
        }
        return roleService.update(role);
    }

    @DeleteMapping
    public int delete(@RequestBody List<Integer> roleIds) {
        return roleService.deleteRoles(roleIds);
    }

    @GetMapping("/list")
    public PageResult list(RoleQuery query) {
        return new PageResult(roleService.count(query), roleService.find(query));
    }

    @GetMapping("/{roleId}")
    public RoleInfo get(@PathVariable Integer roleId) {
        return roleService.findRoleInfo(roleId);
    }

    @PutMapping("/changeStatus")
    public int changeStatus(@RequestBody RoleInfo roleInfo) {
        return roleService.changeStatus(roleInfo);
    }

    @GetMapping("/{roleId}/allocatedUsers")
    public PageResult allocatedUsers(@PathVariable Integer roleId, NameQuery nameQuery) {
        return userRoleService.listAllocatedUsers(roleId, nameQuery);
    }

    @GetMapping("/{roleId}/unallocatedUsers")
    public PageResult unallocatedUsers(@PathVariable Integer roleId, NameQuery nameQuery) {
        return userRoleService.listUnallocatedUsers(roleId, nameQuery);
    }

    @PostMapping("/{roleId}/authUser")
    public void authUser(@PathVariable Integer roleId, @RequestBody List<Long> userIds) {
        userRoleService.authUser(roleId, userIds);
    }

    @DeleteMapping("/{roleId}/authUser")
    public void authUserCancel(@PathVariable Integer roleId, @RequestBody List<Long> userIds) {
        userRoleService.cancelAuth(roleId, userIds);
    }

}
