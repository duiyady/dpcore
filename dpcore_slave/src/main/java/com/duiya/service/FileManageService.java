package com.duiya.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManageService {
    //void test();

    /**
     * 保存图片
     * @param account
     * @param multipartFiles
     * @return
     */
    List<String> saveFile(String account, MultipartFile... multipartFiles);

    /**
     * 获取本机图片
     * @param location
     * @return
     */
    byte[] getFile(String location);

    void saveFile(MultipartFile... multipartFiles);

    boolean hasIp(String ip);
}
