package com.icezhg.athena.service.common.impl;

import com.icezhg.athena.dao.IpLocationDao;
import com.icezhg.athena.domain.IpLocation;
import com.icezhg.athena.service.common.IpLocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by zhongjibing on 2022/12/24.
 */
@Service
public class IpLocationServiceImpl implements IpLocationService {

    private final IpLocationDao ipLocationDao;

    public IpLocationServiceImpl(IpLocationDao ipLocationDao) {
        this.ipLocationDao = ipLocationDao;
    }

    @Override
    public String search(String ip) {
        if (StringUtils.isNotBlank(ip)) {
            IpLocation ipLocation = ipLocationDao.findByIp(ip);
            if (ipLocation != null) {
                return StringUtils.defaultString(ipLocation.getLocation());
            }
        }
        return StringUtils.EMPTY;
    }
}
