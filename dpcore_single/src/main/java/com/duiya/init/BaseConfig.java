package com.duiya.init;

public class BaseConfig {
    /**
     * 文件存储的默认路径
     */
    public static String ROOT_LOCATION = "/Users/duiya/tmp/dpcores/";

    /**
     * 本机的IP+url的hash前6位，为了防止一个主机跑多个服务
     */
    public static String IPHASH6 = "123321";

    /**
     * 上传图片是否需要验证
     */
    public static boolean VERIFIED;

    /**
     * 请求接口是否需要监控时间
     */
    public static boolean MONITOR;

    public static String REDISIP;
    public static int REDISPORT;
    public static String REDISPASS;


}
