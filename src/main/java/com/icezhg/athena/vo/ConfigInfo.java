package com.icezhg.athena.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by zhongjibing on 2021/01/11
 */
@Data
public class ConfigInfo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 32)
    private String name;

    /**
     * 键名
     */
    @NotBlank
    @Size(max = 64)
    private String key;

    /**
     * 键值
     */
    @NotBlank
    @Size(max = 256)
    private String value;

    /**
     * 系统内置（Y是 N否）
     */
    private String type;

    /**
     * 备注
     */
    private String remark;
}
