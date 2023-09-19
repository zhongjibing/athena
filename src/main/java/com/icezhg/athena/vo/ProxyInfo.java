package com.icezhg.athena.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by zhongjibing on 2023/09/20.
 */
@Data
public class ProxyInfo {
    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

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
     * 是否可用
     */
    private Boolean available;

    /**
     * 最后检查时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastCheck;
}
