package com.icezhg.athena.vo;

import com.icezhg.athena.domain.Passwd;
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

    private transient String passwd;

    private transient String salt;

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
        result.setPasswd(passwd != null && !passwd.isBlank() ? passwd : null);
        result.setSalt(salt != null && !salt.isBlank() ? salt : null);
        result.setRemark(remark);
        return result;
    }

}
