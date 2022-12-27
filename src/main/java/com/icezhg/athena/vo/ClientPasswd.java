package com.icezhg.athena.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhongjibing on 2022/12/25.
 */
@Setter
@Getter
public final class ClientPasswd {
    @NotBlank
    private String id;
    @NotBlank
    @Size(max = 32, min = 6)
    private String passwd;

    public ClientPasswd() {
    }

    public ClientPasswd(String id, String passwd) {
        this.id = id;
        this.passwd = passwd;
    }
}
