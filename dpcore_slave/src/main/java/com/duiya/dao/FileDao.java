package com.duiya.dao;

import com.duiya.model.Picture;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface FileDao {

    int save(List<Picture> list);

    List<String> getLocalRecentFile(@Param("last")Long last, @Param("now") Long now,@Param("iphash6") String iphash6);

    void updateState(@Param("location")String location, @Param("iphash6")String iphash6);

    int getAllCount(@Param("account") String account);

    List<String> getPageData(@Param("begin") int begin, @Param("size") int size, @Param("account") String account);
}
