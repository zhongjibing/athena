package com.icezhg.athena.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by zhongjibing on 2022/09/16.
 */
@Data
public class DictTypeInfo {
    /**
     * 字典主键
     */
    private Integer id;

    /**
     * 字典名称
     */
    @NotBlank
    @Size(max = 32)
    private String dictName;

    /**
     * 字典类型
     */
    @NotBlank
    @Size(max = 32)
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
