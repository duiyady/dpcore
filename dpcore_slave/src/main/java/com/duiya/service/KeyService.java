package com.duiya.service;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface KeyService {

    /**
     * 验证密钥
     * @param account
     * @param key
     * @return
     */
    boolean verify(String account, String key);

    /**
     * 获取token
     * @param account
     * @param pass
     * @return
     * @throws Base64DecodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    String getUploadFlag(String account, String pass) throws Base64DecodingException, NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * 删除上传文件token
     * @param account
     */
    void delUpFlag(String account);
}
