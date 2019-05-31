package com.duiya.service;

import com.duiya.cache.RedisCache;
import com.duiya.dao.UserDao;
import com.duiya.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisCache redisCache;


    /**
     * 注册
     * @param account
     * @param password
     * @param publicKey
     * @return 1已经存在用户 0成功
     */
    public int regist(String account, String password, String publicKey, String userEmail){
        int count = userDao.hasAccount(account);
        if(count > 0){
            return 1;
        }
        String passwordM = MD5Util.generateCheckString(password);
        userDao.regist(account, passwordM, publicKey, userEmail);
        return 0;
    }

    /**
     * 登录
     * @param account
     * @param password
     * @return 账户不存在anull 密码错误epass
     */
    public String login(String account, String password, HttpServletRequest request){
        int count = userDao.hasAccount(account);
        if(count == 0){
            return "anull";
        }
        String passwordM = MD5Util.generateCheckString(password);
        Map<String, String> resmap = userDao.login(account, passwordM);
        if(resmap != null){
            String ip = request.getHeader("x-forwarded-for");
            if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("Proxy-Client-IP");
            if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("WL-Proxy-Client-IP");
            if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getRemoteAddr();
            try {
                //管理员
                if(resmap.get("userState").equals("1")){
                    redisCache.putCache("root"+account, ip);
                    return "successr";
                }else {
                    redisCache.putCache(account, ip);
                    return "success";
                }

            } catch (Exception e) {
                return "error";
            }
        }else{
            return "epass";
        }
    }
}
