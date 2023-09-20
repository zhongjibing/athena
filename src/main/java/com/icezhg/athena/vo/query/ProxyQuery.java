package com.icezhg.athena.vo.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by zhongjibing on 2023/09/20.
 */
@Getter
@Setter
public class ProxyQuery extends PageQuery {
    /**
     * 类型
     */
    private String type;

    /**
     * 响应速度
     */
    private Long speed;

    /**
     * 匿名度
     */
    private String anonymity;

    /**
     * 是否可用
     */
    private Boolean available;

    /**
     * 最多检查失败次数
     */
    private Integer maxFailCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
