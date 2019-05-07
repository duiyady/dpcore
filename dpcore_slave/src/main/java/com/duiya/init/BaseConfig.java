package com.duiya.init;

import java.security.Key;

public class BaseConfig {
    /**
     * 文件存储的默认路径
     */
    public static String ROOT_LOCATION;

    /**
     * 主服务器IP
     */
    public static String MASTER_IP;

    /**
     * master的公钥
     */
    public static  Key MASTER_PUBLICKEY;

    /**
     * master的url默认部分
     */
    public static String MASTER_URL;

    /**
     * master的iphash6
     */
    public static String MASTER_IPHASH6;

    /**
     * 本机IP
     */
    public static String LOCAL_IP;

    /**
     * 本机url的默认部分
     */
    public static String LOCAL_URL;

    /**
     * 本机的IP+url的hash前6位，为了防止一个主机跑多个服务
     */
    public static String IPHASH6;

    /**
     * 上传图片是否需要验证
     */
    public static boolean VERIFIED;

    /**
     * 请求接口是否需要监控时间
     */
    public static boolean MONITOR;

    /**
     * 最近一次同步时间
     */
    public static long RECENT_SYNCH;

    /**
     * 公钥
     */
    public static Key PUBLIC_KEY;

    /**
     * 私钥
     */
    public static Key PRIVATE_KEY;

    public static String REDISIP;
    public static int REDISPORT;
    public static String REDISPASS;


}
