package com.icezhg.athena.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 8628704086622756266L;

    private Integer id;
    private String username;
    @JsonIgnore
    private transient String password;
    private String name;
    private String email;
    private String mobile;
    private Date deadline;
    private Date createTime;
    private Date updateTime;
    private Date lastLoginTime;
    private Date credentialsUpdateTime;
    private boolean isDefaultUser;
    private boolean isArchived;
    private boolean isAccountExpired;
    private boolean isAccountLocked;
    private boolean isCredentialsExpired;
}
