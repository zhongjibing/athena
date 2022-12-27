package com.icezhg.athena.vo;

import com.icezhg.athena.annotation.validate.GrantType;
import com.icezhg.athena.annotation.validate.Uri;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * Created by zhongjibing on 2021/01/11
 */
@Data
public class ClientInfo {

    private String id;

    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_-]{2,31}")
    private String clientName;

    @GrantType
    private String authorizationGrantTypes;

    @Uri
    private String redirectUris;

    @Pattern(regexp = "[a-z]{2,32}(,[a-z]{2,32})*")
    private String scopes;

    private String status;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;
}
