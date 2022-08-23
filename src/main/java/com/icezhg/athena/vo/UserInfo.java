package com.icezhg.athena.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by zhongjibing on 2022/08/23.
 */
@Data
public class UserInfo {
    private String name;
    private String nickname;
    private String picture;
    private String profile;
    private List<String> authorities;
}
