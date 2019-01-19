package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.cache.RedisCache;
import com.duiya.init.BaseConfig;
import com.duiya.init.SlaveMess;
import com.duiya.model.Slave;
import com.duiya.utils.RSAUtil;
import com.duiya.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;

@Controller
@RequestMapping("slave")
public class SlaveController {

    private Logger logger = LoggerFactory.getLogger(SlaveController.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册slave
     * @param flag slave用自己的私钥再用master的公钥加密的IPHASH6
     * @param pubKeyStr slave的公钥
     * @param baseUrl slave的url的默认部分
     * @param request
     * @return
     */
    @RequestMapping(value = "regist", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject regist(@RequestParam(value = "flag")String flag,
                             @RequestParam(value = "pubKeyStr")String pubKeyStr,
                             @RequestParam(value = "baseUrl")String baseUrl,
                             HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("WL-Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getRemoteAddr();

        System.out.println("进来啦");

        logger.info("invoke--------------------slave/regist?ip:" + ip);
        String ipHash6  = String.valueOf(ip.hashCode()).substring(0, 6);
        Key pubKey = null;
        String s2 = null;
        try {
            pubKey = RSAUtil.getKey(pubKeyStr);
            String s1 = RSAUtil.decrypt(flag, BaseConfig.PRIVATE_KEY);
            s2 = RSAUtil.decrypt(s1, pubKey);
        } catch (Exception e) {
            logger.error("添加slave失败", e);
        }
        if(s2 != null && s2.equals(ipHash6) || SlaveMess.getSlave(ipHash6) == null){
            Slave slave = new Slave();
            slave.setIP(ip);
            slave.setIPHash6(ipHash6);
            slave.setPublicKey(pubKey);
            slave.setState(1);
            SlaveMess.addSlave(slave);
            redisCache.putPerpetualListCache("slaves", SlaveMess.slaves);
            logger.info("invoke--------------------slave/regist?ip:" + ip +  "success");
            return ResponseUtil.constructOKResponse("success", null);
        }else{
            return ResponseUtil.constructUnknownErrorResponse("添加失败");
        }
    }


    @RequestMapping("test")
    @ResponseBody
    public JSONObject test(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        System.out.println(ip);
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("Proxy-Client-IP");
        System.out.println(ip);
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("WL-Proxy-Client-IP");
        System.out.println(ip);
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getRemoteAddr();

        System.out.println(ip);
        return ResponseUtil.constructOKResponse("success", ip);
    }
}
