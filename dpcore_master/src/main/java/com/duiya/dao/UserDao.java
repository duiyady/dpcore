package com.duiya.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserDao {

    int hasAccount(@Param("account") String account);

    void regist(@Param("account") String account, @Param("password") String password, @Param("publicKey") String publicKey, @Param("email") String email);

    Map<String, String> login(@Param("account") String account, @Param("password") String password);
}
