package com.icezhg.athena.service;

import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.constant.SysConfig;
import com.icezhg.athena.dao.UserDao;
import com.icezhg.athena.domain.User;
import com.icezhg.athena.vo.Query;
import com.icezhg.athena.vo.UserInfo;
import com.icezhg.athena.vo.UserQuery;
import com.icezhg.athena.vo.UserStatus;
import com.icezhg.authorization.core.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Slf4j
@Service
public class UserService {

    private final UserDao userDao;

    private PasswordEncoder passwordEncoder;

    private ConfigService configService;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public boolean checkUnique(UserInfo userInfo) {
        UserQuery query = new UserQuery();
        query.setUsername(userInfo.getUsername());
        List<UserInfo> users = find(query);
        return users.isEmpty() || Objects.equals(userInfo.getId(), users.get(0).getId());
    }

    public UserInfo save(UserInfo userInfo) {
        User newUser = buildUser(userInfo, true);
        newUser.setCreateTime(new Date());
        newUser.setCreateBy(SecurityUtil.currentUserName());
        newUser.setUpdateTime(new Date());
        newUser.setUpdateBy(SecurityUtil.currentUserName());
        userDao.insert(newUser);
        userInfo.setId(newUser.getId());
        return userInfo;
    }

    public UserInfo update(UserInfo userInfo) {
        User existing = buildUser(userInfo, false);
        existing.setUpdateTime(new Date());
        existing.setUpdateBy(SecurityUtil.currentUserName());
        userDao.update(existing);
        return userInfo;
    }

    private User buildUser(UserInfo userInfo, boolean newUser) {
        User user = new User();
        user.setId(userInfo.getId());
        user.setUsername(userInfo.getUsername());
        user.setNickname(userInfo.getNickname());
        user.setGender(userInfo.getGender());
        user.setBirthdate(userInfo.getBirthdate());
        user.setEmail(userInfo.getEmail());
        user.setMobile(userInfo.getMobile());
        user.setRemark(userInfo.getRemark());
        if (newUser) {
            user.setPassword(passwordEncoder.encode(defaultPasswd()));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            user.setDeadline(calendar.getTime());
        }
        return user;
    }

    private String defaultPasswd() {
        return StringUtils.defaultString(configService.findConfig(SysConfig.INIT_PASSWD), Constants.DEFAULT_PASSWD);
    }

    public int count(Query query) {
        return userDao.count(query.toMap());
    }

    public List<UserInfo> find(Query query) {
        return userDao.find(query.toMap());
    }

    public int changeStatus(UserStatus userStatus) {
        User user = new User();
        user.setId(userStatus.userId());
        user.setAccountLocked(userStatus.status());
        user.setUpdateTime(new Date());
        user.setUpdateBy(SecurityUtil.currentUserName());
        return userDao.update(user);
    }

    public UserInfo findById(Long userId) {
        return userDao.findById(userId);
    }
}
