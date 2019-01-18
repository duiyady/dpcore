package com.duiya.utils;

public class ResponseEnum {

    /**
     * 请求成功
     */
    public static final int OK = 0;
    /**
     * ，请求未完全成功，部分失败
     */
    public static final int PART_EROR = -1;
    /**
     * 权限错误 越权访问
     */
    public static final int LIMIT_EROR = -2;
    /**
     * 数据库错误
     */
    public static final int DB_ERROR = -3;
    /**
     * 密钥错误
     */
    public static final int KEY_ERROR = -4;
    /**
     * 未知错误
     */
    public static final int UNKNOEN_ERROR = -5;
    /**
     * 参数错误
     */
    public static final int ARG_ERROR = -6;
    /**
     * 超过负载
     */
    public static final int OVERSIZRE_ERROR = -7;

    /**
     * 密码错误
     */
    public static final int PASSWORD_ERROR = -16;
    /**
     * 验证码错误
     */
    public static final int CODE_ERROR = -17;

    /**
     * 没有图片
     */
    public static final int NULL_PIC = -20;
}
