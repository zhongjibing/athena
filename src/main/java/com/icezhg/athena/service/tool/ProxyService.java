package com.icezhg.athena.service.tool;

import com.icezhg.athena.vo.ProxyInfo;
import com.icezhg.athena.vo.query.Query;

import java.util.List;

/**
 * Created by zhongjibing on 2023/09/20.
 */
public interface ProxyService {

    void addProxy(ProxyInfo data);

    void updateAvailable(ProxyInfo data, Boolean available);

    int count(Query query);

    List<ProxyInfo> find(Query query);

}
