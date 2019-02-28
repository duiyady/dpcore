package com.duiya.dao;

import com.duiya.model.Picture;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface FileDao {

    int save(List<Picture> list);

    List<Map<String, String>> getLocalRecentFile(@Param("last")Long last, @Param("now") Long now,@Param("iphash6") String iphash6);

    void updateState(String location, String iphash6);



}
