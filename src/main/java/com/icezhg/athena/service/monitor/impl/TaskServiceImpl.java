package com.icezhg.athena.service.monitor.impl;

import com.icezhg.athena.dao.TaskDao;
import com.icezhg.athena.domain.Task;
import com.icezhg.athena.quartz.ScheduleUtil;
import com.icezhg.athena.quartz.util.InvokeExpression;
import com.icezhg.athena.service.monitor.TaskService;
import com.icezhg.athena.vo.TaskInfo;
import com.icezhg.athena.vo.query.Query;
import com.icezhg.authorization.core.SecurityUtil;
import com.icezhg.commons.exception.InvalidParameterException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;

    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public TaskInfo findById(Long id) {
        return taskDao.findById(id);
    }

    @Override
    public int count(Query query) {
        return taskDao.count(query.toMap());
    }

    @Override
    public List<TaskInfo> find(Query query) {
        return taskDao.find(query.toMap());
    }

    @Override
    public TaskInfo addTask(TaskInfo taskInfo) {
        if (!InvokeExpression.instanceOf(taskInfo.getInvokeTarget()).isValid()) {
            throw new InvalidParameterException("", "bad invokeTarget");
        }

        Task task = taskInfo.toTask();
        String username = SecurityUtil.currentUserName();
        task.setCreateBy(username);
        task.setUpdateBy(username);
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        int insert = taskDao.insert(task);
        if (insert > 0) {
            taskInfo.setId(task.getId());
            ScheduleUtil.create(taskInfo);
        }
        return findById(task.getId());
    }

    @Override
    public TaskInfo updateTask(TaskInfo taskInfo) {
        if (!InvokeExpression.instanceOf(taskInfo.getInvokeTarget()).isValid()) {
            throw new InvalidParameterException("", "bad invokeTarget");
        }

        Task task = taskInfo.toTask();
        task.setUpdateBy(SecurityUtil.currentUserName());
        task.setUpdateTime(new Date());
        int update = taskDao.update(task);
        if (update > 0) {
            ScheduleUtil.create(taskInfo);
        }
        return findById(taskInfo.getId());
    }

    @Override
    public void removeTask(Long id) {
        TaskInfo taskinfo = taskDao.findById(id);
        if (taskinfo != null) {
            ScheduleUtil.remove(taskinfo);
            taskDao.delete(id);
        }
    }

    @Override
    public void changeTaskStatus(TaskInfo taskInfo) {
        Task task = new Task();
        task.setId(taskInfo.getId());
        task.setStatus(taskInfo.getStatus());
        task.setUpdateBy(SecurityUtil.currentUserName());
        task.setUpdateTime(new Date());
        int update = taskDao.update(task);
        if (update > 0) {
            ScheduleUtil.create(findById(task.getId()));
        }
    }

    @Override
    public void runTask(Long id) {
        TaskInfo task = taskDao.findById(id);
        if (task == null) {
            throw new InvalidParameterException("", "task not exist");
        }
        if (!ScheduleUtil.checkExists(task)) {
            ScheduleUtil.create(task);
        }

        ScheduleUtil.runOnce(task);
    }
}
