package com.duiya.init;

import com.duiya.model.ServerCache;
import com.duiya.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
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
        PropertiesUtil propertiesUtil = new PropertiesUtil("dpcore-master.properties");

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

        Boolean monitor = propertiesUtil.getBooleanValue("dpcore.monitor");
        if(monitor == null){
            monitor = false;
        }
        BaseConfig.MONITOR = monitor;

        BaseConfig.ASYNtime1 = new Date().getTime();
        BaseConfig.ASYNtime2 = BaseConfig.ASYNtime1;

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
        ServerCache master = new ServerCache();
        master.setPUBLICKEY(BaseConfig.PUBLIC_KEY);
        master.setIP(BaseConfig.LOCAL_IP);
        master.setBASEURL(BaseConfig.LOCAL_URL);
        master.setIPHASH6(BaseConfig.IPHASH6);
        redisConnection.set("master", master);
    }

}
