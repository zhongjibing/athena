package com.icezhg.athena.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zhongjibing on 2020/03/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    private Integer id;
    private String name;
    private String description;
    private Integer orderNum;
    private Integer dataScope;
    private Integer menuCheckStrictly;
    private Integer deptCheckStrictly;
    private String status;

    public boolean isRoot() {
        return id != null && id == 0;
    }
}
