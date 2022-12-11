package com.icezhg.athena.service.monitor.impl;

import com.icezhg.athena.dao.TaskLogDao;
import com.icezhg.athena.domain.TaskLog;
import com.icezhg.athena.service.monitor.TaskLogService;
import com.icezhg.athena.vo.TaskLogInfo;
import com.icezhg.athena.vo.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskLogServiceImpl implements TaskLogService {

    private final TaskLogDao taskLogDao;

    public TaskLogServiceImpl(TaskLogDao taskLogDao) {
        this.taskLogDao = taskLogDao;
    }

    @Override
    public TaskLog addLog(TaskLog taskLog) {
        if (taskLog.getCreateTime() == null) {
            taskLog.setCreateTime(new Date());
        }
        taskLogDao.insert(taskLog);
        return taskLog;
    }

    @Override
    public int count(Query query) {
        return taskLogDao.count(query.toMap());
    }

    @Override
    public List<TaskLogInfo> find(Query query) {
        return taskLogDao.find(query.toMap());
    }
}
