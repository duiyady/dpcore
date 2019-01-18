package com.duiya.service.impl;

import com.duiya.service.KeyService;
import org.springframework.stereotype.Service;

@Service
public class KeyServiceImpl implements KeyService {
    @Override
    public String createKey(String account) {
        return "duiya";
    }

    @Override
    public boolean verify(String account, String key) {
        return true;
    }
}
