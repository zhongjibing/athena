package com.icezhg.athena.service;

import com.icezhg.athena.dao.DictDataDao;
import org.springframework.stereotype.Service;

/**
 * 字典 业务层处理
 */
@Service
public class DictDataService {

    private final DictDataDao dictDataDao;

    public DictDataService(DictDataDao dictDataDao) {
        this.dictDataDao = dictDataDao;
    }
}
