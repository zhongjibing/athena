package com.icezhg.athena.controller.system;

import com.icezhg.athena.service.system.RouterService;
import com.icezhg.athena.vo.Router;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/20.
 */
@RestController
@RequestMapping("/routers")
public class RouterController {

    private final RouterService routerService;

    public RouterController(RouterService routerService) {
        this.routerService = routerService;
    }

    @GetMapping
    public List<Router> routers() {
        return routerService.listRouters();
    }
}
