package com.icezhg.athena.domain;

import lombok.Data;

import java.util.Date;

/**
 * ip地理位置
 */
@Data
public class IpLocation {

    /**
     * ip地址
     */
    private String ip;

    /**
     * ip地理位置
     */
    private String location;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
