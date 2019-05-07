package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.model.Slave;
import com.duiya.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlaveServiceImpl implements SlaveService {
    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean hasIp(String ip) {
        List<Slave> slaves = null;
        try {
            slaves = redisCache.getListCache("slaves", Slave.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(slaves != null) {
            for (Slave slave : slaves) {
                if (slave.getIP().equals(ip)) {
                    return true;
                }
            }
        }
        return false;
    }
}
