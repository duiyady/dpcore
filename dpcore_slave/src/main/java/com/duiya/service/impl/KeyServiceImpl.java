package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.dao.UserDao;
import com.duiya.service.KeyService;
import com.duiya.utils.MD5Util;
import com.duiya.utils.RSAUtil;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Service
public class KeyServiceImpl implements KeyService {
    private static final int FLAG_LENGTH = 36;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserDao userDao;

    @Override
    public String createFlag(String account) {
        return "duiya";
    }

    @Override
    public boolean verify(String account, String key){
        if(key.length() != FLAG_LENGTH){
            return false;
        }
        String temp = null;
        try {
            temp = redisCache.getCache("upf" + account, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(temp != null && key.equals(temp)){
            return true;
        }
        return true;
    }

    @Override
    public String getUploadFlag(String account, String pass) throws Base64DecodingException, NoSuchAlgorithmException, InvalidKeySpecException {
        String passw = MD5Util.generateCheckString(pass);
        String keyS = userDao.getUserKey(account, passw);
        if(keyS == null){
            return null;
        }else{
            String uuid = UUID.randomUUID().toString();
            Key key = RSAUtil.getPublicKey(keyS);
            String miwen = RSAUtil.encrypt(uuid, key);
            try {
                redisCache.putCache("upf" + account, uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return miwen;
        }
    }
}
