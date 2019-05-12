package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlaveServiceImpl implements SlaveService {
    @Autowired
    private RedisCache redisCache;

}
