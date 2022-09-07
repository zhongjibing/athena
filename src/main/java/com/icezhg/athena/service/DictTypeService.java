package com.icezhg.athena.service;

import com.icezhg.athena.dao.DictDataDao;
import com.icezhg.athena.dao.DictTypeDao;
import com.icezhg.athena.domain.DictData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 */
@Service
public class DictTypeService {

    private final DictTypeDao dictTypeDao;

    private final DictDataDao dictDataDao;

    public DictTypeService(DictTypeDao dictTypeDao, DictDataDao dictDataDao) {
        this.dictTypeDao = dictTypeDao;
        this.dictDataDao = dictDataDao;
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<DictData> findDictDataByType(String dictType) {
        return dictDataDao.findByType(dictType);
    }
}
