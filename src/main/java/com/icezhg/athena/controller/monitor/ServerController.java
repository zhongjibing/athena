package com.icezhg.athena.controller.monitor;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.server.Server;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2022/09/03.
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    @GetMapping
    @Operation(title = "server monitoring information", type = OperationType.QUERY, saveResult = false)
    public Object serverInfo(String name) throws Exception {
        return StringUtils.hasText(name) ? Server.getInfo(name) : Server.getInfo(Server.ALL);
    }
}
