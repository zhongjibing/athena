package com.icezhg.athena.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by zhongjibing on 2020/03/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 权限字符
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private Integer dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    private Integer menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    private Integer deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
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

    public boolean isAdmin() {
        return id != null && id == 1;
    }
}
