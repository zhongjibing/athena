package com.icezhg.athena.controller.tool;

import com.icezhg.athena.service.tool.ProxyService;
import com.icezhg.athena.vo.PageResult;
import com.icezhg.athena.vo.ProxyInfo;
import com.icezhg.athena.vo.query.ProxyQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhongjibing on 2023/09/20.
 */
@RestController
@RequestMapping("/proxy")
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @PostMapping
    public void add(@RequestBody ProxyInfo data) {
        proxyService.addProxy(data);
    }

    @GetMapping("/list")
    public PageResult list(ProxyQuery query) {
        return new PageResult(proxyService.count(query), proxyService.find(query));
    }

    @GetMapping("/test")
    public Object test() {
        return proxyService.test();
    }
}
