package com.duiya.init;

import com.duiya.model.ResponseModel;
import com.duiya.model.ServerCache;
import com.duiya.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@WebListener
public class Initialize implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(Initialize.class);
    public static PropertiesUtil propertiesUtil = new PropertiesUtil("dpcore-slave.properties");


    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            String s = propertiesUtil.getStringValue("dpcore.ip");
            if(s == null){
                s = IPUtils.getRealIp();
            }
            BaseConfig.LOCAL_IP = s;
        } catch (Exception e) {
            logger.error("获取本机IP失败", e);
            throw new RuntimeException("获取本机IP失败");
        }

        String url = propertiesUtil.getStringValue("dpcore.url");
        BaseConfig.LOCAL_URL = "http://" + BaseConfig.LOCAL_IP + url;
        BaseConfig.IPHASH6 = String.valueOf(BaseConfig.LOCAL_URL.hashCode()).substring(0, 6);

        String root_location = propertiesUtil.getStringValue("dpcore.location");
        if(root_location == null){
            root_location = "/dpcore/file/";
        }
        BaseConfig.ROOT_LOCATION = root_location;
        /*创建目录*/
        File file = new File(BaseConfig.ROOT_LOCATION);
        if(!file.exists() && !file.isDirectory()){
            file.mkdirs();
        }

        Boolean verifide = propertiesUtil.getBooleanValue("dpcore.verified");
        if(verifide == null){
            verifide = false;
        }
        BaseConfig.VERIFIED = verifide;

        Boolean monitor = propertiesUtil.getBooleanValue("dpcore.monitor");
        if(monitor == null){
            monitor = false;
        }
        BaseConfig.MONITOR = monitor;

        BaseConfig.RECENT_SYNCH = new Date().getTime();
        BaseConfig.RECENT_TIME = new Date().getTime();

        try {
            List<Key> keyList = RSAUtil.genKeyPair();
            BaseConfig.PUBLIC_KEY = keyList.get(0);
            BaseConfig.PRIVATE_KEY = keyList.get(1);
        } catch (NoSuchAlgorithmException e) {
            logger.error("生成密钥失败", e);
            throw new RuntimeException("生成密钥失败");
        }


        BaseConfig.REDISIP = propertiesUtil.getStringValue("redis.ip");
        BaseConfig.REDISPORT = propertiesUtil.getIntValue("redis.port");
        BaseConfig.REDISPASS = propertiesUtil.getStringValue("redis.password");
        RedisConnection redisConnection = new RedisConnection(BaseConfig.REDISIP, BaseConfig.REDISPORT, BaseConfig.REDISPASS);
        ServerCache master = null;
        try {
            master = redisConnection.get("master", ServerCache.class);
        } catch (Exception e) {

        }
        if(master != null) {
            BaseConfig.MASTER_IP = master.getIP();
            BaseConfig.MASTER_PUBLICKEY = master.getPUBLICKEY();
            BaseConfig.MASTER_URL = master.getBASEURL();
            BaseConfig.MASTER_IPHASH6 = master.getIPHASH6();

            String flag = null;
            try {
                String iphash6m1 = RSAUtil.encrypt(BaseConfig.IPHASH6, BaseConfig.PRIVATE_KEY);
                flag = RSAUtil.encrypt(iphash6m1, BaseConfig.MASTER_PUBLICKEY);
            } catch (Exception e) {
                logger.error("生成register的flag时失败", e);
            }

            String registurl = BaseConfig.MASTER_URL + "/slave/regist";
            String param = null;
            try {
                param = "flag=" + URLEncoder.encode(flag, "utf8") + "&pubKeyStr=" + URLEncoder.encode(RSAUtil.getString(BaseConfig.PUBLIC_KEY), "utf8") + "&baseUrl=" + URLEncoder.encode(BaseConfig.LOCAL_URL, "utf8");
            } catch (UnsupportedEncodingException e) {
                logger.error("注册时转码失败", e);
            }

            ResponseModel responseModel = null;
            try {
                responseModel = HttpUtil.sendPostModel(registurl, param);
                if (responseModel.getCode() == ResponseEnum.OK) {
                    BaseConfig.RECENT_TIME = new Date().getTime();
                    logger.info("注册成功: ", master.getIP());
                }
            } catch (IOException e) {
                logger.error("向主服务器注册失败", e);
            }
        }else{
            logger.info("系统没有master");
        }
    }

}
