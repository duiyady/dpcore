package com.duiya.init;

import com.duiya.model.ResponseModel;
import com.duiya.model.ServerCache;
import com.duiya.model.Slave;
import com.duiya.service.MonitorService;
import com.duiya.utils.IPUtils;
import com.duiya.utils.PropertiesUtil;
import com.duiya.utils.RSAUtil;
import com.duiya.utils.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@WebListener
public class Initialize implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(Initialize.class);

    public static PropertiesUtil propertiesUtil = new PropertiesUtil("dpcore-master.properties");

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        BaseConfig.redisConnection.del("master");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        /**解析是否设置了ip，若没有设置ip,那用java api获取的是局域网ip*/
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

        BaseConfig.REDISIP = propertiesUtil.getStringValue("redis.ip");
        BaseConfig.REDISPORT = propertiesUtil.getIntValue("redis.port");
        BaseConfig.REDISPASS = propertiesUtil.getStringValue("redis.password");
        BaseConfig.redisConnection = new RedisConnection(BaseConfig.REDISIP, BaseConfig.REDISPORT, BaseConfig.REDISPASS);
        ServerCache master = new ServerCache();
        master.setPUBLICKEY(BaseConfig.PUBLIC_KEY);
        master.setIP(BaseConfig.LOCAL_IP);
        master.setBASEURL(BaseConfig.LOCAL_URL);
        master.setIPHASH6(BaseConfig.IPHASH6);
        try {
            BaseConfig.redisConnection.set("master", master);
        } catch (Exception e) {
            logger.error("缓存master失败", e);
            throw new RuntimeException("缓存master失败");
        }

        /**获取slave集群*/
        try {
            logger.info("======master初始化恢复集群slave端信息======");
            List<Slave> slaveList = BaseConfig.redisConnection.getList("slaves", Slave.class);
            for(Slave slave : slaveList){
                ResponseModel responseModel = MonitorService.upAlive(slave);
                if(responseModel != null){
                    logger.info("======" + slave.getBaseUrl() + "加入集群");
                    SlaveMess.addSlave(slave);
                }
            }
        } catch (Exception e) {
        }
    }

}
