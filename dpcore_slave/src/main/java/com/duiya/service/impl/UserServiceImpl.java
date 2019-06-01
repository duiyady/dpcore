package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean hasUser(String account, String ip, int type) {
        if(type == 0){
            try {
                String ipn = redisCache.getCache(account, String.class);
                if(ipn.equals(ip)){
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }else{
            try {
                String ipn = redisCache.getCache("root"+account, String.class);
                if(ipn.equals(ip)){
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
