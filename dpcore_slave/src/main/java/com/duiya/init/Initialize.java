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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@WebListener
public class Initialize implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(Initialize.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // RedisCache redisCache = new RedisCache();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        PropertiesUtil propertiesUtil = new PropertiesUtil("dpcore-slave.properties");

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String s = addr.getHostAddress();
            BaseConfig.LOCAL_IP = s;
            BaseConfig.IPHASH6 = String.valueOf(s.hashCode()).substring(0, 6);
        } catch (UnknownHostException e) {
            logger.error("获取本机IP失败", e);
            throw new RuntimeException("获取本机IP失败");
        }

        String url = propertiesUtil.getStringValue("dpcore.url");
        BaseConfig.LOCAL_URL = "http://" + BaseConfig.LOCAL_IP + url;

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

        try {
            List<Key> keyList = RSAUtil.genKeyPair();
            BaseConfig.PUBLIC_KEY = keyList.get(0);
            BaseConfig.PRIVATE_KEY = keyList.get(1);
        } catch (NoSuchAlgorithmException e) {
            logger.error("生成密钥失败", e);
            throw new RuntimeException("生成密钥失败");
        }

        String redisip = propertiesUtil.getStringValue("redis.ip");
        int redisport = propertiesUtil.getIntValue("redis.port");
        String redispass = propertiesUtil.getStringValue("redis.password");
        RedisConnection redisConnection = new RedisConnection(redisip, redisport, redispass);
        ServerCache master = redisConnection.get("master", ServerCache.class);
        if(master == null){
            throw new RuntimeException("连接不了主服务器");
        }
        BaseConfig.MASTER_IP = master.getIP();
        BaseConfig.MASTER_PUBLICKEY = master.getPUBLICKEY();
        BaseConfig.MASTER_URL = master.getBASEURL();

        String flag = null;
        try {
            String iphash6m1 = RSAUtil.encrypt(BaseConfig.IPHASH6, BaseConfig.PRIVATE_KEY);
            System.out.println(iphash6m1);
            flag = RSAUtil.encrypt(iphash6m1, BaseConfig.MASTER_PUBLICKEY);
            System.out.println(flag);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("生成register的flag时失败", e);
            throw new RuntimeException("生成register的flag时失败");
        }
        String registurl = BaseConfig.MASTER_URL + "/slave/regist";
        String param = "flag=" + flag + "&publicKey=" + RSAUtil.getString(BaseConfig.PUBLIC_KEY) + "&baseUrl=" + BaseConfig.LOCAL_URL;
        System.out.println("url: " + registurl);
        System.out.println("mess: " + param);
        ResponseModel responseModel = null;
        try {
            responseModel = HttpUtil.sendPostModel(registurl, param);
        } catch (IOException e) {
            logger.error("向主服务器注册失败", e);
            throw new RuntimeException("向主服务器注册失败");
        }
        if(responseModel.getCode() != ResponseEnum.OK){
            throw new RuntimeException("向主服务器注册失败");
        }



    }

}
