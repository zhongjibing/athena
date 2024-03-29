package com.icezhg.athena.service.system.impl;

import com.icezhg.athena.dao.AvatarPictureDao;
import com.icezhg.athena.dao.RoleDao;
import com.icezhg.athena.dao.UserDao;
import com.icezhg.athena.domain.AvatarPicture;
import com.icezhg.athena.domain.Role;
import com.icezhg.athena.domain.User;
import com.icezhg.athena.service.system.ProfileService;
import com.icezhg.athena.vo.Profile;
import com.icezhg.athena.vo.ProfilePasswd;
import com.icezhg.athena.vo.UserInfo;
import com.icezhg.authorization.core.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by zhongjibing on 2022/09/14.
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final AvatarPictureDao avatarPictureDao;

    private PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserDao userDao, RoleDao roleDao, AvatarPictureDao avatarPictureDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.avatarPictureDao = avatarPictureDao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Profile buildProfile() {
        Profile profile = new Profile();
        UserInfo userInfo = userDao.findById(SecurityUtil.currentUserId());
        profile.setId(userInfo.getId());
        profile.setUsername(userInfo.getUsername());
        profile.setNickname(userInfo.getNickname());
        profile.setGender(userInfo.getGender());
        profile.setBirthdate(userInfo.getBirthdate());
        profile.setMobile(userInfo.getMobile());
        profile.setEmail(userInfo.getEmail());
        profile.setAvatar(userInfo.getAvatar());
        profile.setCreateTime(userInfo.getCreateTime());

        String roles = roleDao.findAuthRoles(profile.getId()).stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));
        profile.setRoles(roles);
        return profile;
    }

    @Override
    public void updatePasswd(ProfilePasswd profilePasswd) {
        Long userId = SecurityUtil.currentUserId();
        String passwd = userDao.findUserPasswd(userId);
        if (!passwordEncoder.matches(profilePasswd.oldPassword(), passwd)) {
            throw new AccessDeniedException("wrong passwd");
        }

        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(profilePasswd.newPassword()));
        user.setUpdateTime(new Date());
        user.setUpdateBy(SecurityUtil.currentUserName());
        userDao.update(user);
    }

    @Override
    public Profile updateProfile(Profile profile) {
        User user = new User();
        user.setId(SecurityUtil.currentUserId());
        user.setNickname(profile.getNickname());
        user.setMobile(profile.getMobile());
        user.setEmail(profile.getEmail());
        user.setBirthdate(profile.getBirthdate());
        user.setGender(profile.getGender());
        user.setUpdateTime(new Date());
        user.setUpdateBy(SecurityUtil.currentUserName());
        userDao.update(user);
        return buildProfile();
    }

    @Override
    public void updateAvatar(AvatarPicture avatarPicture) {
        avatarPictureDao.update(avatarPicture);
    }
}
