package com.duiya.init;

import com.duiya.cache.RedisCache;
import com.duiya.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Component
public class DPCoreInit {

    @Autowired
    private static RedisCache redisCache;

    private static Logger logger = LoggerFactory.getLogger(DPCoreInit.class);
    /**
     * 本机的IP hash 前6位
     */
    public static String IPHASH6;

    /**
     * 请求接口是否需要监控时间
     */
    public static boolean MONITOR = PropertiesUtil.getBooleanValue("dpcore.monitor");

    /**
     * 最近一次同步时间
     */
    public static long RECENT_SYNCH;

    /**
     * 公钥
     */
    public static final Key publicKey = null;

    /**
     * 私钥
     */
    public static final Key privateKey = null;

    public static int BALANCEFUN = 0;//0是轮训 1是权重；

    public static long ASYNtime1 = new Date().getTime();

    public static long ASYNtime2 = new Date().getTime();


    static {
        try {
            RECENT_SYNCH = Calendar.getInstance().getTimeInMillis();
            InetAddress addr = InetAddress.getLocalHost();
            String s = addr.getHostAddress();
            IPHASH6 = String.valueOf(s.hashCode()).substring(0, 6);

//            List<Key> key = RSAUtil.genKeyPair();
//            publicKey = key.get(0);
//            privateKey = key.get(1);
//            if(redisCache != null){
//                redisCache.putPerpetualCache("masterip", s);
//                redisCache.putPerpetualCache("masterkey", RSAUtil.getString(publicKey));
//
//            }else{
//                System.out.println("sdfasgva");
//            }

//        }catch (NoSuchAlgorithmException e){
//            logger.error("获取密钥失败");
//            throw new RuntimeException("获取密钥失败");
        }catch (UnknownHostException e) {
            logger.error("获取本机ip失败", e);
            throw new RuntimeException("获取ip失败");
        }
    }

}
