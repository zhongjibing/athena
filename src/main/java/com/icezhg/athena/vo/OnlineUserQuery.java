package com.icezhg.athena.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OnlineUserQuery extends PageQuery {
    private String username;
    private String loginIp;
}
