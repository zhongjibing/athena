package com.icezhg.athena.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhongjibing on 2022/09/04.
 */
@Setter
@Getter
public class RoleQuery extends PageQuery {
    private String name;
    private String status;
}
