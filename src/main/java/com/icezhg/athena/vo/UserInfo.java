package com.icezhg.athena.vo;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhongjibing on 2020/03/15
 */
@Data
public class UserInfo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

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
     * 创建时间
     */
    private Date createTime;

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

    public boolean isAccountExpired() {
        return deadline != null && deadline.getTime() < Calendar.getInstance().getTimeInMillis();
    }

    public boolean isCredentialsExpired() {
        Date credentialsCreateTime = credentialsUpdateTime != null ? credentialsUpdateTime : createTime;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -6);
        return credentialsCreateTime.getTime() < calendar.getTimeInMillis();
    }

    public String getAccountLocked() {
        return String.valueOf(accountLocked);
    }

    public void setAccountLocked(Integer accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setAccountLocked(String accountLocked) {
        if (accountLocked != null && accountLocked.length() > 0) {
            this.accountLocked = Integer.parseInt(accountLocked);
        }
    }
}
