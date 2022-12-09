package com.icezhg.athena.service.monitor;

import com.icezhg.athena.domain.TaskLog;
import com.icezhg.athena.vo.query.Query;

import java.util.List;

public interface TaskLogService {

    TaskLog addLog(TaskLog taskLog);

    int count(Query query);

    List<TaskLog> find(Query query);
}
