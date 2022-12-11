package com.icezhg.athena.controller.monitor;

import com.icezhg.athena.service.monitor.TaskLogService;
import com.icezhg.athena.service.monitor.TaskService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.TaskInfo;
import com.icezhg.athena.vo.query.TaskLogQuery;
import com.icezhg.athena.vo.query.TaskQuery;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor/task")
public class TaskController {

    private final TaskService taskService;

    private final TaskLogService taskLogService;

    public TaskController(TaskService taskService, TaskLogService taskLogService) {
        this.taskService = taskService;
        this.taskLogService = taskLogService;
    }

    @PostMapping
    public TaskInfo addTask(@RequestBody TaskInfo task) {
        return taskService.addTask(task);
    }

    @PutMapping
    public TaskInfo updateTask(@RequestBody TaskInfo task) {
        return taskService.updateTask(task);
    }

    @GetMapping("/{taskId}")
    public TaskInfo taskInfo(@PathVariable Long taskId) {
        return taskService.findById(taskId);
    }

    @DeleteMapping("/{taskId}")
    public void removeTask(@PathVariable Long taskId) {
        taskService.removeTask(taskId);
    }

    @GetMapping("/list")
    public PageResult list(TaskQuery query) {
        return new PageResult(taskService.count(query), taskService.find(query));
    }

    @PutMapping("/changeStatus")
    public void changeTaskStatus(@RequestBody TaskInfo task) {
        taskService.changeTaskStatus(task);
    }

    @PostMapping("/run")
    public void runTask(Long taskId) {
        taskService.runTask(taskId);
    }

    @GetMapping("/log/list")
    public PageResult listLogs(TaskLogQuery query) {
        return new PageResult(taskLogService.count(query), taskLogService.find(query));
    }

}
