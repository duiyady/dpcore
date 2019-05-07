package com.duiya.init;

import com.duiya.utils.RedisConnection;

import java.security.Key;
import java.util.Date;

public class BaseConfig {
    /**
     * 主服务器IP
     */
    public static String LOCAL_IP;

    /**
     * master的公钥
     */
    public static  Key PUBLIC_KEY;

    /**
     * master的私钥
     */
    public static  Key PRIVATE_KEY;

    /**
     * master的url默认部分
     */
    public static String LOCAL_URL;

    /**
     * 本机的IP+url hash前6位
     */
    public static String IPHASH6;

    /**
     * 请求接口是否需要监控时间
     */
    public static boolean MONITOR;

    /**
     * 负载均衡模式
     */
    public static int BALANCEFUN = 0;//0是轮训 1是权重；

    /**
     * 同步开始时间
     */
    public static long ASYNtime1 = new Date().getTime();

    /**
     * 同步结束时间
     */
    public static long ASYNtime2 = new Date().getTime();

    public static String REDISIP;
    public static int REDISPORT;
    public static String REDISPASS;

    public static RedisConnection redisConnection;

}
