package com.icezhg.athena.service.system;

import com.icezhg.athena.vo.ClientInfo;
import com.icezhg.athena.vo.ClientPasswd;
import com.icezhg.athena.vo.ClientStatus;
import com.icezhg.athena.vo.query.Query;

import java.util.List;

/**
 * Created by zhongjibing on 2022/12/25.
 */
public interface ClientService {
    boolean checkUnique(ClientInfo client);

    ClientInfo save(ClientInfo client);

    ClientInfo update(ClientInfo client);

    ClientInfo findById(String id);

    int count(Query query);

    List<ClientInfo> find(Query query);

    int changeStatus(ClientStatus clientStatus);

    int resetPasswd(ClientPasswd clientPasswd);
}
