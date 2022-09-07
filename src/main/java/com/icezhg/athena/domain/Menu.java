package com.icezhg.athena.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity {

    private Integer id;
    private String name;
    private Integer parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private String query;
    private Integer isFrame;
    private Integer isCache;
    private String type;
    private String visible;
    private String status;
    private String perms;
    private String icon;
}
