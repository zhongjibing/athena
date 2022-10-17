package com.icezhg.athena.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OnlineUser {
    private String sessionId;
    private String id;
    private String username;
    private String gender;
    private String birthdate;
    private String loginIp;
    private String mobile;
    private String picture;
    private String nickname;
    private String email;
    private String createTime;
    private String lastAccessedTime;
    private String loginTime;
    private String userAgent;
    private List<String> aud;
}
