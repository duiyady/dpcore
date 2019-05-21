package com.duiya.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    String getUserKey(String account, String pass);

}
