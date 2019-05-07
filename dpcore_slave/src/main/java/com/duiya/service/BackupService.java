package com.duiya.service;

import com.duiya.model.Location;

import javax.servlet.ServletOutputStream;

public interface BackupService {

    /**
     * 本地没有这个图片时请求同步
     * @param location
     * @return
     */
    byte[] getFile(Location location);

    /**
     * 数据备份
     * @param last 上一次时间
     * @param now 这一次时间
     */
    void aync(Long last, Long now);

    /**
     * 当用户请求图片但本机没有时，向其他服务器请求备份，将备份写入本地并且写入用户的返回流
     * @param location
     * @param outputStream
     */
    void getAndWriteFile(Location location, String path, ServletOutputStream outputStream) throws Exception;
}
