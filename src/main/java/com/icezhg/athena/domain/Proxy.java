package com.icezhg.athena.domain;

import lombok.Data;

import java.util.Date;

/**
 * 代理地址
 * <p>
 * Created by zhongjibing on 2023/09/20.
 */
@Data
public class Proxy {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private int port;

    /**
     * 类型
     */
    private String type;

    /**
     * 响应速度
     */
    private Long speed;

    /**
     * 位置
     */
    private String location;

    /**
     * 匿名度
     */
    private String anonymity;

    /**
     * 最后检查时间
     */
    private Date lastCheck;

    /**
     * 是否可用
     */
    private Boolean available;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
