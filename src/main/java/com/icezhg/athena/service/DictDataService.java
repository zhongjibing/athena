package com.icezhg.athena.service;

import com.icezhg.athena.domain.DictData;
import com.icezhg.athena.vo.DictDataInfo;
import com.icezhg.athena.vo.DictQuery;

import java.util.List;

/**
 * 字典 业务层处理
 */
public interface DictDataService {

    DictDataInfo save(DictDataInfo dataInfo);

    DictDataInfo update(DictDataInfo dataInfo);

    DictDataInfo findById(Integer id);

    int count(DictQuery query);

    List<DictData> find(DictQuery query);

    int deleteByIds(List<Integer> ids);


}
