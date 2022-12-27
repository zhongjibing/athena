package com.icezhg.athena.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created by zhongjibing on 2022/12/25.
 */
@Data
public final class ClientStatus {
    @NotBlank
    private String id;

    @NotBlank
    private String status;
}
