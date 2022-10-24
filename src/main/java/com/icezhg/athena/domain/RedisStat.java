package com.icezhg.athena.domain;

import lombok.Data;

import java.util.Date;


@Data
public class RedisStat {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String cmdName;

    /**
     *
     */
    private String value;

    /**
     *
     */
    private Date createTime;

}
