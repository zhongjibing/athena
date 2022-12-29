package com.icezhg.athena.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;


@Data
public class Passwd {

    /**
     * id
     */
    private String id;

    /**
     * 主题
     */
    private String title;

    /**
     * 密码
     */
    @JsonIgnore
    private transient String passwd;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
