package com.icezhg.athena.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Data
public class RoleInfo {
    private Integer id;
    @NotBlank
    @Size(max = 16)
    private String name;
    @NotBlank
    @Size(max = 32)
    private String description;
    private Integer orderNum;
    private Integer dataScope;
    private boolean menuCheckStrictly;
    private boolean deptCheckStrictly;
    private String status;
    private String remark;
    private List<Integer> menuIds;

}
