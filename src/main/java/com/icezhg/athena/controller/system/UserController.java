package com.icezhg.athena.controller.system;

import com.icezhg.athena.service.system.UserRoleService;
import com.icezhg.athena.service.system.UserService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.UserAuth;
import com.icezhg.athena.vo.UserInfo;
import com.icezhg.athena.vo.UserPasswd;
import com.icezhg.athena.vo.UserQuery;
import com.icezhg.athena.vo.UserStatus;
import com.icezhg.commons.exception.ErrorCodeException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/10.
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public UserInfo add(@Validated @RequestBody UserInfo user) {
        if (!userService.checkUnique(user)) {
            throw new ErrorCodeException("", "username is already exists");
        }
        return userService.save(user);
    }

    @PutMapping
    public UserInfo edit(@Validated @RequestBody UserInfo user) {
        if (!userService.checkUnique(user)) {
            throw new ErrorCodeException("", "username is already exists");
        }
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public UserInfo get(@PathVariable Long userId) {
        return userService.findById(userId);
    }


    @GetMapping("/list")
    public PageResult list(UserQuery query) {
        return new PageResult(userService.count(query), userService.find(query));
    }

    @PutMapping("/changeStatus")
    public int changeStatus(@RequestBody UserStatus userStatus) {
        return userService.changeStatus(userStatus);
    }

    @PutMapping("/resetPasswd")
    public int resetPasswd(@RequestBody UserPasswd userPasswd) {
        return userService.resetPasswd(userPasswd);
    }

    @GetMapping("/{userId}/auth")
    public UserAuth findUserAuth(@PathVariable Long userId) {
        return userRoleService.findAuth(userId);
    }

    @PutMapping("/{userId}/auth")
    public UserAuth updateUserAuth(@PathVariable Long userId, @RequestBody List<Integer> roleIds) {
        return userRoleService.updateUserAuth(userId, roleIds);
    }
}
