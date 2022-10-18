package com.icezhg.athena.service.monitor;

import com.icezhg.athena.vo.query.OnlineUserQuery;
import com.icezhg.athena.vo.PageResult;

public interface OnlineUserService {

    PageResult listOnlineUsers(OnlineUserQuery query);
}
