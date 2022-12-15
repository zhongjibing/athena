package com.icezhg.athena.service.monitor;

import com.icezhg.athena.domain.LoginRecord;
import com.icezhg.athena.vo.query.Query;

import java.util.List;

public interface LoginRecordService {

    int count(Query query);

    List<LoginRecord> find(Query query);
}
