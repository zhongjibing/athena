package com.icezhg.athena.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhongjibing on 2021/10/28
 */
@Data
public class LoginInfo implements Serializable {
    private static final long serialVersionUID = 951313246165598434L;

    private String username;
    private String password;
}
