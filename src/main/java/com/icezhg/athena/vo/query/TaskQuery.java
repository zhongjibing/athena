package com.icezhg.athena.vo.query;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TaskQuery extends PageQuery {
    private String taskName;
    private String taskGroup;
    private String status;
}
