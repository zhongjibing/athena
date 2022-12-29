package com.icezhg.athena.dao;

import com.icezhg.athena.domain.Passwd;
import com.icezhg.athena.vo.PasswdInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongjibing on 2022/12/30.
 */
@Repository
public interface PasswdDao {

    PasswdInfo findById(String id);

    int deleteById(String id);

    int insert(Passwd record);

    int update(Passwd record);

    int count(Map<String, Object> query);

    List<PasswdInfo> find(Map<String, Object> query);

}
