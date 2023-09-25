package com.icezhg.athena.task;

import com.alibaba.fastjson2.JSONObject;
import com.icezhg.athena.service.tool.ProxyService;
import com.icezhg.athena.vo.ProxyInfo;
import com.icezhg.athena.vo.query.ProxyQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Created by zhongjibing on 2023/09/20.
 */
@Component
public class ProxyAvailableCheckTask {
    private static final Logger log = LoggerFactory.getLogger(ProxyAvailableCheckTask.class);

    private final ProxyService proxyService;

    @Value("${proxy.request.url}")
    private String requestUrl;

    @Value("${proxy.local-address:}")
    private Set<String> localAddress;

    @Value("${proxy.update-disabled:false}")
    private boolean updateDisabled;

    public ProxyAvailableCheckTask(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    public void check() {
        log.info("start check proxy available status...");
        ProxyQuery query = new ProxyQuery();
        query.setMaxFailCount(10);
        query.setPageSize(100);
        List<ProxyInfo> proxies;
        while (!(proxies = proxyService.find(query)).isEmpty()) {
            proxies.forEach(this::check);
            query.setPageNum(query.getPageNum() + 1);
        }
        log.info("finish check proxy available status");
    }


    private void check(ProxyInfo proxy) {
        Proxy.Type type = proxy.getType().startsWith("http") ? Proxy.Type.HTTP : Proxy.Type.SOCKS;
        InetSocketAddress address = InetSocketAddress.createUnresolved(proxy.getIp(), proxy.getPort());
        ProxySelector proxySelector = new CustomProxySelector(type, address);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .proxy(proxySelector)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.requestUrl))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        StopWatch watch = new StopWatch();
        watch.start();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(res -> {
                    if (res.statusCode() != HttpStatus.OK.value()) {
                        return false;
                    }

                    JSONObject body = JSONObject.parseObject(res.body());
                    return body != null && !localAddress.contains((String) body.getOrDefault("address", "<None>"));
                })
                .exceptionally(throwable -> false)
                .thenAccept(available -> {
                    watch.stop();
                    proxy.setSpeed(watch.getTotalTimeMillis());

                    log.info("check proxy {}/{}:{} available: {}, speed: {}", proxy.getIp(), proxy.getType(),
                            proxy.getPort(), available, proxy.getSpeed());
                    if (!updateDisabled) {
                        proxyService.updateAvailable(proxy, available);
                    }
                });
    }


    static class CustomProxySelector extends ProxySelector {
        final List<Proxy> list;

        CustomProxySelector(Proxy.Type type, InetSocketAddress address) {
            list = List.of(new Proxy(type, address));
        }

        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException e) {
            /* ignore */
        }

        @Override
        public synchronized List<Proxy> select(URI uri) {
            return list;
        }
    }
}
