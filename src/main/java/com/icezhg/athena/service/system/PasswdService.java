package com.icezhg.athena.service.system;

import com.icezhg.athena.vo.PasswdInfo;
import com.icezhg.athena.vo.query.Query;

import java.util.List;

/**
 * Created by zhongjibing on 2022/12/30.
 */
public interface PasswdService {
    PasswdInfo save(PasswdInfo entity);

    PasswdInfo update(PasswdInfo entity);

    PasswdInfo findPasswd(String id, String secretKey);

    int count(Query query);

    List<PasswdInfo> find(Query query);

    List<String> generate(int size);
}
