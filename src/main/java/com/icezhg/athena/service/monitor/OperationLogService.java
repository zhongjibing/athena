package com.icezhg.athena.service.monitor;

import com.icezhg.athena.domain.OperationLog;
import com.icezhg.athena.vo.OperationInfo;
import com.icezhg.athena.vo.query.Query;

import java.util.List;

/**
 * Created by zhongjibing on 2022/12/24.
 */
public interface OperationLogService {
    OperationLog addLog(OperationLog operationLog);

    int count(Query query);

    List<OperationInfo> find(Query query);
}
