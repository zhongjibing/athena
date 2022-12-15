package com.icezhg.athena.service.monitor.impl;

import com.icezhg.athena.dao.LoginRecordDao;
import com.icezhg.athena.domain.LoginRecord;
import com.icezhg.athena.service.monitor.LoginRecordService;
import com.icezhg.athena.vo.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {

    private final LoginRecordDao loginRecordDao;

    public LoginRecordServiceImpl(LoginRecordDao loginRecordDao) {
        this.loginRecordDao = loginRecordDao;
    }

    @Override
    public int count(Query query) {
        return loginRecordDao.count(query.toMap());
    }

    @Override
    public List<LoginRecord> find(Query query) {
        return loginRecordDao.find(query.toMap());
    }
}
