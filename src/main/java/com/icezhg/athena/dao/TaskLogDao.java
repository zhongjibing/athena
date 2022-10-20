package com.icezhg.athena.dao;


import com.icezhg.athena.domain.TaskLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskLogDao {

    int insert(TaskLog record);

    int count(Map<String, Object> query);

    List<TaskLog> find(Map<String, Object> query);

}
