package com.icezhg.athena.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Data
public class Menu {

    private Integer id;
    private String name;
    private Integer parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private String query;
    private Integer frame;
    private Integer cache;
    private String type;
    private String visible;
    private String status;
    private String perms;
    private String icon;
    private String createBy;
    private String updateBy;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
