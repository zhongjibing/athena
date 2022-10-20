package com.icezhg.athena.service.monitor.impl;

import com.icezhg.athena.dao.TaskDao;
import com.icezhg.athena.domain.Task;
import com.icezhg.athena.service.monitor.TaskService;
import com.icezhg.athena.vo.TaskInfo;
import com.icezhg.athena.vo.query.Query;
import com.icezhg.authorization.core.SecurityUtil;
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
        Task task = taskInfo.toTask();
        String username = SecurityUtil.currentUserName();
        task.setCreateBy(username);
        task.setUpdateBy(username);
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        return findById(task.getId());
    }

    @Override
    public TaskInfo updateTask(TaskInfo taskInfo) {
        Task task = taskInfo.toTask();
        task.setUpdateBy(SecurityUtil.currentUserName());
        task.setUpdateTime(new Date());
        taskDao.update(task);
        return findById(taskInfo.getId());
    }

    @Override
    public void removeTask(Long id) {
        taskDao.delete(id);
    }

    @Override
    public void changeTaskStatus(TaskInfo taskInfo) {
        Task task = new Task();
        task.setId(taskInfo.getId());
        task.setStatus(taskInfo.getStatus());
        task.setUpdateBy(SecurityUtil.currentUserName());
        task.setUpdateTime(new Date());
        taskDao.update(task);
    }

    @Override
    public void runTask(Long id) {

    }
}
