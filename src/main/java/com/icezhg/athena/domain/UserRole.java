package com.icezhg.athena.domain;

import lombok.Data;

/**
 * Created by zhongjibing on 2022/09/13.
 */
@Data
public class UserRole {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Integer roleId;
}
