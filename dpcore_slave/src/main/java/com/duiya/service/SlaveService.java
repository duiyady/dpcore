package com.duiya.service;

public interface SlaveService {

    /**
     * 通过ip判断是否有这个slave
     * @param ip
     * @return
     */
    boolean hasIp(String ip);
}
