package com.icezhg.athena.service.system.impl;

import com.icezhg.athena.constant.PasswdConfig;
import com.icezhg.athena.dao.PasswdDao;
import com.icezhg.athena.domain.Passwd;
import com.icezhg.athena.service.system.PasswdService;
import com.icezhg.athena.util.PasswdGenerator;
import com.icezhg.athena.vo.PasswdInfo;
import com.icezhg.athena.vo.query.Query;
import com.icezhg.authorization.core.SecurityUtil;
import com.icezhg.commons.exception.InvalidAccessException;
import com.icezhg.commons.util.IdGenerator;
import com.icezhg.encryptor.SM4;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhongjibing on 2022/12/30.
 */
@Service
public class PasswdServiceImpl implements PasswdService {

    private final PasswdDao passwdDao;

    private PasswordEncoder passwordEncoder;

    public PasswdServiceImpl(PasswdDao passwdDao) {
        this.passwdDao = passwdDao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PasswdInfo save(PasswdInfo entity) {
        Passwd passwd = entity.toPasswd();
        SM4 sm4 = SM4.getInstance(PasswdConfig.getSalt());
        String encrypt = sm4.encrypt(entity.getPasswd());
        passwd.setSalt(encrypt.substring(0, 8));
        passwd.setPasswd(encrypt.substring(8));
        passwd.setId(IdGenerator.nextId());
        String username = SecurityUtil.currentUserName();
        passwd.setCreateBy(username);
        passwd.setUpdateBy(username);
        passwd.setCreateTime(new Date());
        passwd.setUpdateTime(new Date());
        passwdDao.insert(passwd);
        return passwdDao.findById(passwd.getId());
    }

    @Override
    public PasswdInfo update(PasswdInfo entity) {
        Passwd passwd = entity.toPasswd();
        if (StringUtils.isBlank(entity.getPasswd())) {
            SM4 sm4 = SM4.getInstance(PasswdConfig.getSalt());
            String encrypt = sm4.encrypt(entity.getPasswd());
            passwd.setSalt(encrypt.substring(0, 8));
            passwd.setPasswd(encrypt.substring(8));
        }
        String username = SecurityUtil.currentUserName();
        passwd.setUpdateBy(username);
        passwd.setUpdateTime(new Date());
        passwdDao.update(passwd);
        return passwdDao.findById(passwd.getId());
    }

    @Override
    public PasswdInfo findPasswd(String id, String secretKey) {
        if (!passwordEncoder.matches(secretKey, PasswdConfig.getSecret())) {
            throw new InvalidAccessException("", "access denied");
        }

        return passwdDao.findById(id);
    }

    @Override
    public int count(Query query) {
        return passwdDao.count(query.toMap());
    }

    @Override
    public List<PasswdInfo> find(Query query) {
        return passwdDao.find(query.toMap());
    }

    @Override
    public List<String> generate(int size) {
        PasswdGenerator generator = new PasswdGenerator(size);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(generator.generate());
        }
        return result;
    }
}
