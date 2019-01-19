package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.dao.FileDao;
import com.duiya.init.BaseConfig;
import com.duiya.model.Location;
import com.duiya.model.Picture;
import com.duiya.model.Slave;
import com.duiya.service.FileManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileManageServiceImpl implements FileManageService {

    private Logger logger = LoggerFactory.getLogger(FileManageServiceImpl.class);

    @Autowired
    private FileDao fileDao;

    @Autowired
    private RedisCache redisCache;

    @Override
    /**
     * 普通用户保存图片，返回值是成功的数目
     * 第一次存储服务器IP的Hash前6位/时间(xxxxyyzz格式)/分/UUID前8位+秒
     */
    public List<String> saveFile(String account, MultipartFile... multipartFiles) {
        int flag = 0;
        List<Picture> data = new LinkedList<>();
        List<String> result = new ArrayList<>();
        for(int i = 0; i < multipartFiles.length; i++){
            MultipartFile mf = multipartFiles[i];
            if(!mf.isEmpty()){
                Picture picture = new Picture();
                List<Object> list = Location.getFileName(BaseConfig.IPHASH6, BaseConfig.ROOT_LOCATION);
                picture.setFileName(String.valueOf(list.get(0)));
                picture.setFileTime(Long.valueOf(String.valueOf(list.get(2))));
                picture.setFileFserver(BaseConfig.IPHASH6);
                picture.setFileOwner(account);
                picture.setFileState(BaseConfig.IPHASH6 );
                try {
                    File ft = new File(String.valueOf(list.get(1)));
                    if(!ft.exists()){
                        ft.mkdirs();
                    }
                    mf.transferTo(ft);
                    result.add(picture.getFileName());
                } catch (IOException e) {
                    picture.setFileState("null");
                    logger.error("保存图片失败", e);
                    flag = 1;
                }
                data.add(picture);
            }
        }
        if(flag == 1){
            return null;
        }else{
            int res = fileDao.save(data);
            if(res >= 1) {
                return result;
            }else{
                return null;
            }
        }
    }

    @Override
    public byte[] getFile(String location) {
        byte[] bFile = null;
        try {
            bFile = Files.readAllBytes(Paths.get(location));
        } catch (IOException e) {
            logger.error("获取图片失败", e);
            e.printStackTrace();
        }
        return bFile;
    }

    @Override
    public void saveFile(MultipartFile... multipartFiles) {
        for(int i = 0; i < multipartFiles.length; i++){
            MultipartFile mf = multipartFiles[i];
            if(!mf.isEmpty()){
                String contentType=mf.getContentType();
                String imageName = contentType.substring(contentType.indexOf("/")+1);
                String filePath = Location.getPath(imageName, BaseConfig.ROOT_LOCATION);
                try {
                    File ft = new File(filePath);
                    if(!ft.exists()){
                        ft.mkdirs();
                    }
                    mf.transferTo(ft);
                } catch (IOException e) {
                    logger.error("保存图片失败", e);
                }
            }
        }
    }

    @Override
    public boolean hasIp(String ip) {
        List<Slave> slaves = redisCache.getListCache("slaves", Slave.class);
        if(slaves != null){
            for(int i = 0; i < slaves.size(); i++){
                if(slaves.get(i).getIP().equals(ip)){
                    return true;
                }
            }
        }
        return false;
    }
}
