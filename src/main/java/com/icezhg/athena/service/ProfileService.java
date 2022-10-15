package com.icezhg.athena.service;

import com.icezhg.athena.domain.AvatarPicture;
import com.icezhg.athena.vo.Profile;
import com.icezhg.athena.vo.ProfilePasswd;

/**
 * Created by zhongjibing on 2022/09/14.
 */
public interface ProfileService {

    Profile buildProfile();

    void updatePasswd(ProfilePasswd profilePasswd);

    Profile updateProfile(Profile profile);

    void updateAvatar(AvatarPicture avatarPicture);
}
