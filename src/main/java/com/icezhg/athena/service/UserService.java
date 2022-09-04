package com.icezhg.athena.service;

import com.icezhg.athena.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleService roleService;


}
