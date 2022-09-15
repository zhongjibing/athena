package com.icezhg.athena.controller.system;

import com.icezhg.athena.domain.AvatarPicture;
import com.icezhg.athena.domain.Picture;
import com.icezhg.athena.service.ConfigService;
import com.icezhg.athena.service.PictureService;
import com.icezhg.athena.service.ProfileService;
import com.icezhg.athena.vo.Profile;
import com.icezhg.athena.vo.ProfilePasswd;
import com.icezhg.authorization.core.SecurityUtil;
import com.icezhg.authorization.core.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by zhongjibing on 2022/09/14.
 */
@RestController
@RequestMapping("/system/user/profile")
public class ProfileController {

    private final ProfileService profileService;

    private PictureService pictureService;

    private ConfigService configService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public Profile profile() {
        return profileService.buildProfile();
    }

    @PutMapping
    public Profile updateProfile(@Validated @RequestBody Profile profile) {
        return profileService.updateProfile(profile);
    }


    @PostMapping("/updatePasswd")
    public void updatePasswd(@RequestBody ProfilePasswd profilePasswd) {
        profileService.updatePasswd(profilePasswd);
    }

    @PostMapping("/avatar")
    public Profile avatar(MultipartFile file) {
        UserInfo userInfo = SecurityUtil.currentUserInfo();
        Picture picture = pictureService.save(file);
        AvatarPicture avatarPicture = new AvatarPicture(userInfo.getPicture(), picture.getId());
        profileService.updateAvatar(avatarPicture);
        return profileService.buildProfile();
    }
}
