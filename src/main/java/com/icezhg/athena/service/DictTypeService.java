package com.icezhg.athena.service;

import com.icezhg.athena.domain.DictData;
import com.icezhg.athena.domain.DictType;
import com.icezhg.athena.vo.DictQuery;
import com.icezhg.athena.vo.DictTypeInfo;

import java.util.List;

/**
 * 字典 业务层处理
 */
public interface DictTypeService {


    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<DictData> findDictDataByType(String dictType);

    int count(DictQuery query);

    List<DictType> find(DictQuery query);

    DictTypeInfo findById(Integer id);

    boolean checkUnique(DictTypeInfo typeInfo);

    DictTypeInfo save(DictTypeInfo typeInfo);

    DictTypeInfo update(DictTypeInfo typeInfo);

    int deleteByIds(List<Integer> ids);

    List<DictTypeInfo> listOptions();
}
