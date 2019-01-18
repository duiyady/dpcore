package com.duiya.service;

public interface KeyService {
    /**
     * 创建一个密钥
     * @param account
     * @return
     */
    String createKey(String account);

    /**
     * 验证密钥
     * @param account
     * @param key
     * @return
     */
    boolean verify(String account, String key);
}
