package com.icezhg.athena.controller.system;

import com.icezhg.athena.service.ProfileService;
import com.icezhg.athena.vo.Profile;
import com.icezhg.athena.vo.ProfilePasswd;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2022/09/14.
 */
@RestController
@RequestMapping("/system/user/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
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
}
