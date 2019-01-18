package com.duiya.init;

import com.duiya.cache.RedisCache;
import com.duiya.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.Calendar;

@Component
public class DPCoreInit {

    @Autowired
    private static RedisCache redisCache;

    private static Logger logger = LoggerFactory.getLogger(DPCoreInit.class);
    /**
     * 文件存储的默认路径
     */
    public static String ROOT_LOCATION = PropertiesUtil.getStringValue("dpcore.location");

    /**
     * 主服务器IP
     */
    public static String MASTER_IP;

    /**
     * 上传图片是否需要验证
     */
    public static boolean VERIFIED = PropertiesUtil.getBooleanValue("dpcore.verified");

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
    public static Key publicKey;

    /**
     * 私钥
     */
    public static Key privateKey;

    public static  Key masterPublicKey;


    static {
        try {
            RECENT_SYNCH = Calendar.getInstance().getTimeInMillis();
            InetAddress addr = InetAddress.getLocalHost();
            String s = addr.getHostAddress();
            IPHASH6 = String.valueOf(s.hashCode()).substring(0, 6);
//
//            List<Key> key = RSAUtil.genKeyPair();
//            publicKey = key.get(0);
//            privateKey = key.get(1);
//            String ss = redisCache.getCache("masterkey", String.class);
//            if(ss == null){
//                throw new RuntimeException("没有主服务器");
//            }
//            masterPublicKey = RSAUtil.getKey(ss);
//            ss = redisCache.getCache("masterip", String.class);
//            if(ss == null){
//                throw new RuntimeException("没有主服务器");
//            }
//            MASTER_IP = ss;
//
//            RECENT_SYNCH = new Date().getTime();

//        }catch (NoSuchAlgorithmException e){
//            logger.error("获取密钥失败");
//            throw new RuntimeException("获取密钥失败");
        }catch (UnknownHostException e) {
            logger.error("获取本机ip失败", e);
            throw new RuntimeException("获取ip失败");
        } catch (IOException e) {
            throw new RuntimeException("初始化失败");
        } catch (Exception e) {
            throw new RuntimeException("初始化失败");
        }
    }

}
