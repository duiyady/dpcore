package com.duiya.service.impl;

import com.duiya.cache.RedisCache;
import com.duiya.dao.FileDao;
import com.duiya.init.BaseConfig;
import com.duiya.model.Location;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class BackupServiceImpl implements BackupService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private FileDao fileDao;

    @Override
    public byte[] getFile(Location location) {
        String host = redisCache.getCache(location.getIPHash6(), String.class);
        if(host == null){
            return null;
        }
        String key = "slave" + location.getIPHash6();
        ServerCache slave = redisCache.getCache(key, ServerCache.class);
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
        //这里的iphash6要换成
        List<Map<String, String>> list = fileDao.getLocalRecentFile(last, now, "156800");
        List<String> filePath = new ArrayList<>();

        //获取所有的slave;
        List<Slave> slaveList = redisCache.getListCache("slaves", Slave.class);






    }

    @Override
    public void getAndWriteFile(Location location, String path, ServletOutputStream outputStream) {
        String host = redisCache.getCache(location.getIPHash6(), String.class);
        if(host == null){
            return;
        }
        String key = "slave" + location.getIPHash6();
        ServerCache slave = redisCache.getCache(key, ServerCache.class);
        if(slave == null){
            return;
        }

        String url = slave.getBASEURL() + "/backup/get";
        String param = null;
        try {
            param = "location=" + URLEncoder.encode(location.getFull(), "utf8");
        } catch (UnsupportedEncodingException e) {
            logger.error("备份数据编码location失败:", e);
        }
        InputStream in = null;
        BufferedReader bf = null;
        BufferedWriter bfw = null;
        try {
            try {
                in = HttpUtil.sendGetByte(url, param);
            } catch (Exception e) {
                logger.error("发送http get请求失败", e);
                return;
            }
            if (in != null) {
               bf = new BufferedReader(new InputStreamReader(in));
                try {
                    String flag = bf.readLine();
                    if (flag != null && flag.equals(ResponseEnum.PIC_BACKUPOK)) {
                        String lo = bf.readLine();
                        if (lo.equals(location.getFull())) {
                            File file = new File(path);
                            bfw = new BufferedWriter(new FileWriter(file));
                            String line = null;
                            while ((line = bf.readLine()) != null) {
                                outputStream.print(line);
                                bfw.write(line);

                            }
                            bfw.flush();
                        }
                        fileDao.updateState(location.getFull(), "," + BaseConfig.IPHASH6);
                    } else {
                        /*对方服务器也没有找到*/
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }finally {
            if(bf != null){
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bfw != null){
                try {
                    bfw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return;
    }
}
