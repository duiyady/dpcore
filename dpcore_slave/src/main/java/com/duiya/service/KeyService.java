package com.duiya.service;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface KeyService {
    /**
     * 创建一个密钥
     * @param account
     * @return
     */
    String createFlag(String account);

    /**
     * 验证密钥
     * @param account
     * @param key
     * @return
     */
    boolean verify(String account, String key);

    String getUploadFlag(String account, String pass) throws Base64DecodingException, NoSuchAlgorithmException, InvalidKeySpecException;
}
