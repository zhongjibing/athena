package com.icezhg.athena.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Data
public class User {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private transient String password;

    /**
     * 姓名
     */
    private String nickname;

    /**
     * 用户性别: 0.女, 1.男
     */
    private String gender;

    /**
     * 生日
     */
    private String birthdate;

    /**
     * 邮件
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 有效时间
     */
    private Date deadline;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 密码更新时间
     */
    private Date credentialsUpdateTime;

    /**
     * 是否锁定: 0.否, 1.是
     */
    private Integer accountLocked;

    /**
     * 备注
     */
    private String remark;

    public boolean isRoot() {
        return isRoot(this.id);
    }

    public static boolean isRoot(Long userId) {
        return userId != null && userId == 0L;
    }
}
