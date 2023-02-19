package com.icezhg.athena.service.system.impl;

import com.icezhg.athena.constant.PasswdConfig;
import com.icezhg.athena.constant.SysConfig;
import com.icezhg.athena.dao.PasswdDao;
import com.icezhg.athena.domain.Passwd;
import com.icezhg.athena.service.system.ConfigService;
import com.icezhg.athena.service.system.PasswdService;
import com.icezhg.athena.util.PasswdGenerator;
import com.icezhg.athena.vo.PasswdInfo;
import com.icezhg.athena.vo.query.Query;
import com.icezhg.authorization.core.SecurityUtil;
import com.icezhg.commons.exception.InvalidAccessException;
import com.icezhg.commons.util.IdGenerator;
import com.icezhg.encryptor.SMUtil;
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

    private ConfigService configService;

    private PasswordEncoder passwordEncoder;

    public PasswdServiceImpl(PasswdDao passwdDao) {
        this.passwdDao = passwdDao;
    }

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PasswdInfo save(PasswdInfo entity) {
        Passwd passwd = encrypt(entity).toPasswd();
        passwd.setId(IdGenerator.nextId());
        String username = SecurityUtil.currentUserName();
        passwd.setCreateBy(username);
        passwd.setUpdateBy(username);
        passwd.setCreateTime(new Date());
        passwd.setUpdateTime(new Date());
        passwdDao.insert(passwd);
        return erasePasswd(passwdDao.findById(passwd.getId()));
    }

    @Override
    public PasswdInfo update(PasswdInfo entity) {
        Passwd passwd = encrypt(entity).toPasswd();
        String username = SecurityUtil.currentUserName();
        passwd.setUpdateBy(username);
        passwd.setUpdateTime(new Date());
        passwdDao.update(passwd);
        return erasePasswd(passwdDao.findById(passwd.getId()));
    }

    @Override
    public PasswdInfo findById(String id) {
        return erasePasswd(passwdDao.findById(id));
    }

    @Override
    public PasswdInfo findPasswd(String id, String secretKey) {
        String ds = configService.findConfig(SysConfig.PASSWD_SM4_SALT);
        String decrypt = SMUtil.sm4Decrypt(ds.substring(1 << 1 + 1), secretKey);
        if (!passwordEncoder.matches(decrypt, PasswdConfig.getSecret())) {
            throw new InvalidAccessException("", "access denied");
        }

        return decrypt(passwdDao.findById(id));
    }

    @Override
    public int count(Query query) {
        return passwdDao.count(query.toMap());
    }

    @Override
    public List<PasswdInfo> find(Query query) {
        List<PasswdInfo> passwdList = passwdDao.find(query.toMap());
        passwdList.forEach(this::erasePasswd);
        return passwdList;
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

    @Override
    public int deleteByIds(List<String> ids) {
        return passwdDao.deleteByIds(ids);
    }

    private PasswdInfo decrypt(PasswdInfo passwd) {
        if (passwd == null) {
            return null;
        }

        char[] cipher = passwd.getSalt().toCharArray();
        for (int i = 0; i < cipher.length; i++) {
            cipher[i] = (char) (cipher[i] - i % 2);
        }
        String data = new String(cipher) + passwd.getPasswd();
        String plain = SMUtil.sm4Decrypt(PasswdConfig.getSalt(), data);
        String ds = configService.findConfig(SysConfig.PASSWD_SM4_SALT);
        String encrypt = new String(SMUtil.sm4Encrypt(ds.substring(1 << 1 + 1), plain));
        passwd.setSalt(encrypt.substring(0, 8));
        passwd.setPasswd(encrypt.substring(8));
        return passwd;
    }

    private PasswdInfo encrypt(PasswdInfo passwd) {
        if (StringUtils.isBlank(passwd.getPasswd()) || StringUtils.isBlank(passwd.getSalt())) {
            return erasePasswd(passwd);
        }

        String data = passwd.getSalt() + passwd.getPasswd();
        String ds = configService.findConfig(SysConfig.PASSWD_SM4_SALT);
        String plain = SMUtil.sm4Decrypt(ds.substring(1 << 1 + 1), data);
        char[] bytes = new String(SMUtil.sm4Encrypt(PasswdConfig.getSalt(), plain)).toCharArray();
        for (int i = 0; i < 8; i++) {
            bytes[i] = (char) (bytes[i] + i % 2);
        }
        String cipher = new String(bytes);
        passwd.setSalt(cipher.substring(0, 8));
        passwd.setPasswd(cipher.substring(8));
        return passwd;
    }

    private PasswdInfo erasePasswd(PasswdInfo passwdInfo) {
        if (passwdInfo != null) {
            passwdInfo.setPasswd(null);
            passwdInfo.setSalt(null);
        }
        return passwdInfo;
    }

}
