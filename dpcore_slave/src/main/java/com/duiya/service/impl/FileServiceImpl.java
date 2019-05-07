package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.dao.FileDao;
import com.duiya.init.BaseConfig;
import com.duiya.model.Location;
import com.duiya.model.Picture;
import com.duiya.service.FileService;
import com.duiya.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileDao fileDao;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Map<Integer, String> saveFile(String account, MultipartFile... multipartFiles) {
        List<Picture> data = new LinkedList<>();
        Map<Integer, String> result = new HashMap<>();
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
                } catch (IOException e) {
                    picture.setFileState("null");
                    logger.error("保存图片失败", e);
                }
                result.put(i, picture.getFileName());
                data.add(picture);
            }
        }
        fileDao.save(data);
        if(result.size() > 0){
            return result;
        }else{
            return null;
        }
    }

    @Override
    public void getAndWriteFile(String path, ServletOutputStream outputStream) throws Exception {
        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(new File(path)));
        byte[] temp = new byte[1024];
        int len = 0;
        while((len = bf.read(temp)) != -1){
            outputStream.write(temp, 0, len);
        }
        if(bf != null){
            bf.close();
        }
    }

    @Override
    public void getAndWriteFileL(ServletOutputStream outputStream, String path) throws Exception {
        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(new File(path)));
        int available = bf.available();
        outputStream.println(StringUtil.getLengthString(available, 10));
        byte[] temp = new byte[1024];
        int len = 0;
        while((len = bf.read(temp)) != -1){
            outputStream.write(temp, 0, len);
        }
        if(bf != null){
            bf.close();
        }
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
}
