package com.duiya.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    String getUserKey(@Param("account") String account, @Param("pass") String pass);

}
