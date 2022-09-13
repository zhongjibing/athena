package com.icezhg.athena.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Created by zhongjibing on 2022/09/14.
 */
@Data
public class Profile {

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
    @NotBlank
    private String nickname;

    /**
     * 用户性别: 0.女, 1.男
     */
    @NotBlank
    private String gender;

    /**
     * 生日
     */
    private String birthdate;

    /**
     * 邮件
     */
    @NotBlank
    private String email;

    /**
     * 手机号
     */
    @NotBlank
    private String mobile;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 所属角色
     */
    private String roles;
}
