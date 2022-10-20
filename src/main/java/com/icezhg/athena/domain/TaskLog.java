package com.icezhg.athena.domain;

import lombok.Data;

import java.util.Date;

/**
 * 定时任务调度日志表
 */
@Data
public class TaskLog {
    /**
     * 任务日志ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务组名
     */
    private String taskGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * 日志信息
     */
    private String message;

    /**
     * 执行状态（0正常 1失败）
     */
    private String status;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 创建时间
     */
    private Date createTime;

}
