package com.duiya.dao;

import com.duiya.model.Picture;

import java.util.List;


public interface FileDao {

    int save(List<Picture> list);

    List<String> getLocalRecentFile(Long last, Long now);



}
