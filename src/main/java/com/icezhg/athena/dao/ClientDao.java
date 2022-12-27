package com.icezhg.athena.dao;

import com.icezhg.athena.domain.Client;
import com.icezhg.athena.vo.ClientInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongjibing on 2022/12/25.
 */
@Repository
public interface ClientDao {

    int insert(Client client);

    int update(Client client);

    ClientInfo findById(String clientId);

    int count(Map<String, Object> query);

    List<ClientInfo> find(Map<String, Object> query);
}
