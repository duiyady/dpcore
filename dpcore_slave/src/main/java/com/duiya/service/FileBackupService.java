package com.duiya.service;

import com.duiya.model.Location;

public interface FileBackupService {

    /**
     * 本地没有这个图片时请求同步
     * @param location
     * @return
     */
    boolean getFile(Location location);

    /**
     * 数据备份
     * @param last 上一次时间
     * @param now 这一次时间
     */
    void aync(Long last, Long now);
}
