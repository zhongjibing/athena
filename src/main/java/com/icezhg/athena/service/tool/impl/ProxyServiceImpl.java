package com.icezhg.athena.service.tool.impl;

import com.icezhg.athena.dao.ProxyDao;
import com.icezhg.athena.domain.Proxy;
import com.icezhg.athena.service.tool.ProxyService;
import com.icezhg.athena.vo.ProxyInfo;
import com.icezhg.athena.vo.query.Query;
import com.icezhg.authorization.core.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zhongjibing on 2023/09/20.
 */
@Service
public class ProxyServiceImpl implements ProxyService {
    private static final Logger log = LoggerFactory.getLogger(ProxyServiceImpl.class);

    private final ProxyDao proxyDao;

    public ProxyServiceImpl(ProxyDao proxyDao) {
        this.proxyDao = proxyDao;
    }

    @Override
    public void addProxy(ProxyInfo data) {
        if (isValid(data)) {
            Proxy record = buildProxy(data);
            Proxy existing = proxyDao.findByIpAndPort(record.getIp(), record.getPort());
            if (existing == null) {
                proxyDao.insert(record);
            } else {
                record.setId(existing.getId());
                proxyDao.update(record);
            }
        }
    }

    @Override
    public void updateAvailable(ProxyInfo data, Boolean available) {
        Proxy record = new Proxy();
        record.setId(data.getId());
        record.setAvailable(available != null && available);
        record.setLastCheck(new Date());
        record.setUpdateTime(new Date());
        proxyDao.update(record);
    }

    @Override
    public int count(Query query) {
        return proxyDao.count(query.toMap());
    }

    @Override
    public List<ProxyInfo> find(Query query) {
        return proxyDao.find(query.toMap()).stream().map(this::buildProxyInfo).toList();
    }

    @Override
    public Object test() {
        log.info("proxy test from remote: {}", IpUtil.getRequestIp());
        return Map.of("timestamp", new Date());
    }

    private ProxyInfo buildProxyInfo(Proxy proxy) {
        ProxyInfo info = new ProxyInfo();
        info.setId(proxy.getId());
        info.setIp(proxy.getIp());
        info.setPort(proxy.getPort());
        info.setType(proxy.getType());
        info.setSpeed(proxy.getSpeed());
        info.setLocation(proxy.getLocation());
        info.setAnonymity(proxy.getAnonymity());
        info.setAvailable(proxy.isAvailable());
        info.setLastCheck(proxy.getLastCheck());
        return info;
    }

    private Proxy buildProxy(ProxyInfo data) {
        Proxy proxy = new Proxy();
        proxy.setId(data.getId());
        proxy.setIp(data.getIp());
        proxy.setPort(Optional.ofNullable(data.getPort()).orElse(0));
        proxy.setType(data.getType());
        proxy.setSpeed(Optional.ofNullable(data.getSpeed()).orElse(0L));
        proxy.setLocation(data.getLocation());
        proxy.setLastCheck(data.getLastCheck());
        proxy.setAnonymity(data.getAnonymity());
        proxy.setAvailable(data.getAvailable());
        proxy.setCreateTime(new Date());
        proxy.setUpdateTime(new Date());
        return proxy;
    }

    private boolean isValid(ProxyInfo data) {
        return StringUtils.isNotBlank(data.getIp())
                && data.getPort() != null && data.getPort() > 0
                && StringUtils.isNotBlank(data.getType());
    }
}
