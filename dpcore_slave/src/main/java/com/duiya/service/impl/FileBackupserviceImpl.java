package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.dao.FileDao;
import com.duiya.init.BaseConfig;
import com.duiya.model.Location;
import com.duiya.model.ResponseModel;
import com.duiya.model.Slave;
import com.duiya.service.FileBackupService;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileBackupserviceImpl implements FileBackupService {

    private Logger logger = LoggerFactory.getLogger(FileManageServiceImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private FileDao fileDao;

    @Override
    public boolean getFile(Location location) {
        String host = redisCache.getCache(location.getIPHash6(), String.class);
        if(host == null){
            return false;
        }
        String url = "http://" + host + ":8080/dpcore_slave_war/corefile/get";
        String param = location.getFull();
        ResponseModel responseModel = null;
        try {
            responseModel = HttpUtil.sendGetModel(url, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(responseModel == null || responseModel.getCode() != ResponseEnum.OK){
            return false;
        }else{
            byte[] byt = (byte[]) responseModel.getData();
            File file = new File(location.getPath(BaseConfig.ROOT_LOCATION));
            if(!file.exists()){
                file.mkdirs();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(byt);
                fos.close();
            } catch (Exception e) {
                logger.error("请求远程服务器获得图片写入失败", e);
                return false;
            }finally {
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    @Override
    @Async
    public void aync(Long last, Long now) {
        //可能上次没有成功，掉线，但是也只同步启动到当时
        if(BaseConfig.RECENT_SYNCH < last){
            last = BaseConfig.RECENT_SYNCH;
        }
        List<String> fileLocation = fileDao.getLocalRecentFile(last, now);
        List<String> filePath = new ArrayList<>();
        for(String s : fileLocation){
            filePath.add(Location.getPath(s, BaseConfig.ROOT_LOCATION));
        }
        List<Slave> slaves = redisCache.getListCache("slaves", Slave.class);
        String preUrl = "http://";
        String lastUrl = ":8080/";


    }
}
