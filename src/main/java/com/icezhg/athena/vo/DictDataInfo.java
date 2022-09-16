package com.icezhg.athena.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by zhongjibing on 2022/09/17.
 */
@Data
public class DictDataInfo {
    /**
     * 字典编码
     */
    private Integer id;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    @NotBlank
    @Size(max = 32)
    private String dictLabel;

    /**
     * 字典键值
     */
    @NotBlank
    @Size(max = 64)
    private String dictValue;

    /**
     * 字典键值类型
     */
    private String dictValueType;

    /**
     * 字典类型
     */
    @NotBlank
    @Size(max = 32)
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
    /**
     * 备注
     */
    private String remark;
}
