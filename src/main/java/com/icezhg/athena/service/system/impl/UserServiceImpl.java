package com.icezhg.athena.service.system.impl;

import com.icezhg.athena.constant.PasswdConfig;
import com.icezhg.athena.constant.SysConfig;
import com.icezhg.athena.dao.AvatarPictureDao;
import com.icezhg.athena.dao.UserDao;
import com.icezhg.athena.domain.AvatarPicture;
import com.icezhg.athena.domain.User;
import com.icezhg.athena.service.system.ConfigService;
import com.icezhg.athena.service.system.UserService;
import com.icezhg.athena.util.MaskSensitiveUtil;
import com.icezhg.athena.vo.UserInfo;
import com.icezhg.athena.vo.UserPasswd;
import com.icezhg.athena.vo.UserStatus;
import com.icezhg.athena.vo.query.Query;
import com.icezhg.athena.vo.query.UserQuery;
import com.icezhg.authorization.core.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final AvatarPictureDao avatarPictureDao;

    private PasswordEncoder passwordEncoder;

    private ConfigService configService;

    public UserServiceImpl(UserDao userDao, AvatarPictureDao avatarPictureDao) {
        this.userDao = userDao;
        this.avatarPictureDao = avatarPictureDao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean checkUnique(UserInfo userInfo) {
        UserQuery query = new UserQuery();
        query.setUsername(userInfo.getUsername());
        List<UserInfo> users = find(query);
        return users.isEmpty() || (users.size() == 1 && Objects.equals(userInfo.getId(), users.get(0).getId()));
    }

    @Override
    @Transactional
    public UserInfo save(UserInfo userInfo) {
        AvatarPicture avatarPicture = defaultAvatarPicture();
        avatarPictureDao.create(avatarPicture);

        User newUser = buildUser(userInfo, true);
        newUser.setAvatar(avatarPicture.avatar());
        newUser.setCreateTime(new Date());
        newUser.setCreateBy(SecurityUtil.currentUserName());
        newUser.setUpdateTime(new Date());
        newUser.setUpdateBy(SecurityUtil.currentUserName());
        userDao.insert(newUser);
        userInfo.setId(newUser.getId());
        return userInfo;
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        User existing = buildUser(userInfo, false);
        existing.setUpdateTime(new Date());
        existing.setUpdateBy(SecurityUtil.currentUserName());
        userDao.update(existing);
        return userInfo;
    }

    private AvatarPicture defaultAvatarPicture() {
        return new AvatarPicture(configService.findConfig(SysConfig.DEFAULT_AVATAR_PICTURE));
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
        String initUserPasswd = configService.findConfig(SysConfig.INIT_USER_PASSWD);
        return StringUtils.defaultString(initUserPasswd, PasswdConfig.userInitPasswd());
    }

    @Override
    public int count(Query query) {
        return userDao.count(query.toMap());
    }

    @Override
    public List<UserInfo> find(Query query) {
        List<UserInfo> userInfos = userDao.find(query.toMap());
        userInfos.forEach(user -> user.setMobile(MaskSensitiveUtil.maskMobile(user.getMobile())));
        return userInfos;
    }

    @Override
    public int changeStatus(UserStatus userStatus) {
        User user = new User();
        user.setId(userStatus.userId());
        user.setAccountLocked(userStatus.status());
        user.setUpdateTime(new Date());
        user.setUpdateBy(SecurityUtil.currentUserName());
        return userDao.update(user);
    }

    @Override
    public UserInfo findById(Long userId) {
        UserInfo userInfo = userDao.findById(userId);
        if (userInfo != null) {
            userInfo.setMobile(MaskSensitiveUtil.maskMobile(userInfo.getMobile()));
        }

        return userInfo;
    }

    @Override
    public int resetPasswd(UserPasswd userPasswd) {
        User user = new User();
        user.setId(userPasswd.userId());
        user.setPassword(passwordEncoder.encode(userPasswd.passwd()));
        user.setUpdateTime(new Date());
        user.setUpdateBy(SecurityUtil.currentUserName());
        return userDao.update(user);
    }

}
