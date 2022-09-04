package com.icezhg.athena.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhongjibing on 2020/03/18
 */
@Data
public class Role {

    private Integer id;
    private String name;
    private String description;
    private Integer sortIndex;
    private Integer dataScope;
    private Integer menuCheckStrictly;
    private Integer deptCheckStrictly;
    private String status;
    private String createBy;
    private String updateBy;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
