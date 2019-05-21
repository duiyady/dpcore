package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.dao.FileDao;
import com.duiya.init.BaseConfig;
import com.duiya.model.Location;
import com.duiya.model.ResponseModel;
import com.duiya.model.ServerCache;
import com.duiya.model.Slave;
import com.duiya.service.BackupService;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
public class BackupServiceImpl implements BackupService {

    private Logger logger = LoggerFactory.getLogger(BackupServiceImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private FileDao fileDao;

    @Override
    public byte[] getFile(Location location) {
        String host = null;
        try {
            host = redisCache.getCache(location.getIPHash6(), String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(host == null){
            return null;
        }
        String key = "slave" + location.getIPHash6();
        ServerCache slave = null;
        try {
            slave = redisCache.getCache(key, ServerCache.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(slave == null){
            return null;
        }

        String url = slave.getBASEURL() + "/backup/get";
        String param = null;
        try {
            param = "location=" + URLEncoder.encode(location.getFull(), "utf8");
        } catch (UnsupportedEncodingException e) {
            logger.error("备份数据编码location失败:", e);
        }
        byte[] result;
        try {
            //result = HttpUtil.sendGetByte(url, param);
        } catch (Exception e) {
            logger.error("发送http get请求失败", e);
        }
        /*未完成*/
        return null;
    }

    @Override
    @Async
    public void aync(Long last, Long now) {
        //可能上次没有成功，掉线，但是也只同步启动到当时
        if(BaseConfig.RECENT_SYNCH < last){
            last = BaseConfig.RECENT_SYNCH;
        }
        //这里的iphash6要换成,获取本机的图片
        List<String> list = null;
        list = fileDao.getLocalRecentFile(last, now, BaseConfig.IPHASH6);

        Map<String, String> fileMess = Location.getFileMess(list, BaseConfig.ROOT_LOCATION);

        //获取所有的slave;
        List<Slave> slaveList = null;
        try {
            slaveList = redisCache.getListCache("slaves", Slave.class);
        } catch (Exception e) {

        }
        Iterator<Map.Entry<String, String>> iterator = fileMess.entrySet().iterator();
        Map<String, String> temp = new HashMap<>();
        int count = 1;
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if(count%30 == 0){
                for(Slave slave : slaveList){
                    if(!slave.getIPHash6().equals(BaseConfig.IPHASH6)) {
                        logger.info("invoke--------------------backupservice/aync? " + count + "---" + last + "--->" + now + ":" + slave.getBaseUrl());
                        String baseurl = slave.getBaseUrl();
                        String urlstr = baseurl + "/backup/put";
                        HttpUtil.sendPostImage(urlstr, null, temp, null);
                        logger.info("success--------------------backupservice/aync?" + count + "---" + last + "--->" + now + ":" + slave.getBaseUrl());
                    }
                }
                temp = new HashMap<>();
                temp.put(entry.getKey(), entry.getValue());
            }else{
                temp.put(entry.getKey(), entry.getValue());
            }
            count++;
        }

        for(Slave slave : slaveList){
            if(!slave.getIPHash6().equals(BaseConfig.IPHASH6)) {
                logger.info("invoke--------------------backupservice/aync? " + count + "---" + last + "--->" + now + ":" + slave.getBaseUrl());
                String baseurl = slave.getBaseUrl();
                String urlstr = baseurl + "/backup/put";
                HttpUtil.sendPostImage(urlstr, null, temp, null);
                logger.info("success--------------------backupservice/aync?" + count + "---" + last + "--->" + now + ":" + slave.getBaseUrl());
            }
        }
    }

    @Override
    public void getAndWriteFile(Location location, String path, ServletOutputStream outputStream) throws Exception {
        List<Slave> slaves = redisCache.getListCache("slaves", Slave.class);
        String param = "location=" + URLEncoder.encode(location.getFull(), "utf8");
        boolean success = false;
        if (slaves != null) {
            for (Slave slave : slaves) {
                //这个服务器不是本机
                if (!(slave.getIPHash6().equals(BaseConfig.IPHASH6))) {
                    String url = slave.getBaseUrl() + "/backup/hasFile";
                    ResponseModel responseModel = HttpUtil.sendGetModel(url, param);
                    //这个服务器有这个文件
                    if (responseModel.getCode() == ResponseEnum.OK) {
                        url = slave.getBaseUrl() + "/file/get";
                        InputStream in = null;//对方服务器的额输入流
                        OutputStream out = null;//写入本机的输入流
                        try {
                            in = HttpUtil.sendGetByte(url, param);
                            if (in != null) {
                                File file = new File(path);
                                if(!file.exists()){
                                    File tempf = new File(location.getPathNoName(BaseConfig.ROOT_LOCATION));
                                    tempf.mkdirs();
                                }

                                byte[] bytes = new byte[1024];
                                out = new BufferedOutputStream(new FileOutputStream(file));
                                int len = 0;
                                while((len = in.read(bytes)) != -1) {
                                    outputStream.write(bytes, 0, len);
                                    out.write(bytes, 0, len);
                                }
                                success = true;
                                out.flush();
                                out.close();

                            } else {
                                //输入流为空
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                //如果已经同步成功
                if (success == true) {
                    fileDao.updateState(location.getFull(), "," + BaseConfig.IPHASH6);
                    break;
                }
            }
        }
    }

}
