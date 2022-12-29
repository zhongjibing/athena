package com.icezhg.athena.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icezhg.athena.domain.Passwd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;


@Data
public class PasswdInfo {

    /**
     * id
     */
    private String id;

    /**
     * 主题
     */
    private String title;

    @NotBlank
    @JsonIgnore
    @Size(min = 6, max = 32)
    private transient String passwd;

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

    public Passwd toPasswd() {
        Passwd result = new Passwd();
        result.setId(id);
        result.setTitle(title);
        result.setPasswd(passwd);
        result.setRemark(remark);
        return result;
    }

}
