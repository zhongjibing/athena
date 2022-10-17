package com.icezhg.athena.service.monitor;

import com.icezhg.athena.vo.OnlineUserQuery;
import com.icezhg.athena.vo.PageResult;

public interface OnlineUserService {

    PageResult listOnlineUsers(OnlineUserQuery query);
}
