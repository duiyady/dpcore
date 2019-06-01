package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.cache.RedisCache;
import com.duiya.init.BaseConfig;
import com.duiya.init.SlaveMess;
import com.duiya.model.Slave;
import com.duiya.service.SlaveServie;
import com.duiya.service.UserService;
import com.duiya.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    private SlaveServie slaveServie;

    @Autowired
    private UserService userServie;


    /**
     * 注册slave
     * @param flag slave用自己的私钥再用master的公钥加密的IPHASH6
     * @param pubKeyStr slave的公钥
     * @param baseUrl slave的url的默认部分
     * @param request
     * @return
     */
    @RequestMapping(value = "regist")
    @ResponseBody
    public JSONObject regist(@RequestParam(value = "flag", required = false)String flag,
                             @RequestParam(value = "pubKeyStr", required = false)String pubKeyStr,
                             @RequestParam(value = "baseUrl", required = false)String baseUrl,
                             HttpServletRequest request){

        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("WL-Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getRemoteAddr();

        if(StringUtil.isBlank(flag, pubKeyStr, baseUrl) == true){
            return ResponseUtil.constructArgErrorResponse("参数不对");
        }
        logger.info("invoke--------------------slave/regist?ip:" + ip);
        String ipHash6  = String.valueOf(baseUrl.hashCode()).substring(0, 6);
        Key pubKey = null;
        String s2 = null;
        try {
            pubKey = RSAUtil.getPublicKey(pubKeyStr);
            String s1 = RSAUtil.decrypt(flag, BaseConfig.PRIVATE_KEY);
            s2 = RSAUtil.decrypt(s1, pubKey);
        } catch (Exception e) {
            logger.error("添加slave失败", e);
            e.printStackTrace();
            return ResponseUtil.constructUnknownErrorResponse("key error");
        }


        if(s2 != null && s2.equals(ipHash6)){
            Slave slave = new Slave();
            slave.setIP(ip);
            slave.setIPHash6(ipHash6);
            slave.setPublicKey(pubKey);
            slave.setBaseUrl(baseUrl);
            slave.setState(1);
            SlaveMess.addSlave(slave);
            logger.info("invoke--------------------slave/regist?ip:" + ip +  "success");
            return ResponseUtil.constructOKResponse("success", null);
        }else{
            return ResponseUtil.constructUnknownErrorResponse("添加失败");
        }
    }


    /**
     * 修改slave状态
     * @param iphash6
     * @param state 2或10
     * @return
     */
    @RequestMapping(value = "changeslavestate")
    @ResponseBody
    public JSONObject changeSlaveState(@RequestParam(value = "iphash6")String iphash6,
                                       @RequestParam(value = "state")int state,
                                       @RequestParam(value = "account", required = false) String account,
                                       HttpServletRequest request) {
        logger.info("invoke--------------------slave/changeslavestate?iphash6:" + iphash6 + ",state:" + state + ",account:" + account);

        if(iphash6.length() != 6){
            return ResponseUtil.constructArgErrorResponse("iphash6值不对");
        }
        if(state != 1 && state != 10){
            return ResponseUtil.constructArgErrorResponse("状态码错误");
        }

        if(SlaveMess.hasSlaveByIp(iphash6) == false){
            return ResponseUtil.constructArgErrorResponse("iphash6不对");
        }

        String ip = HttpUtil.getIp(request);
        boolean login = userServie.hasUser(account, ip, 1);
        if(login){
            slaveServie.changeSlaveState(state, iphash6);
            return ResponseUtil.constructOKResponse("success", null);
        }else{
            return ResponseUtil.constructNoUserResponse();
        }

    }

    /**
     * 修改负载均衡策略
     * @param blance
     * @param account
     * @return
     */
    @RequestMapping(value = "changeblance")
    @ResponseBody
    public JSONObject changeblance(@RequestParam(value = "blance")Integer blance,
                                   @RequestParam(value = "account", required = false) String account,
                                   HttpServletRequest request){

        if(blance != 0 && blance != 1){
            return ResponseUtil.constructArgErrorResponse("策略错误");
        }

        String ip = HttpUtil.getIp(request);
        boolean login = userServie.hasUser(account, ip, 1);
        if(login) {
            slaveServie.changeblancetype(blance);
            return ResponseUtil.constructOKResponse("success", null);
        }else{
            return ResponseUtil.constructNoUserResponse();
        }
    }

    /**
     * 返回slaves信息
     * @param account
     * @return
     */
    @RequestMapping(value = "getslaves")
    @ResponseBody
    public JSONObject getSlaves(@RequestParam(value = "account", required = false)String account,
                                HttpServletRequest request){
        String ip = HttpUtil.getIp(request);
        boolean login = userServie.hasUser(account, ip, 1);
        if(login) {
            return ResponseUtil.constructResponse(ResponseEnum.OK, "success", SlaveMess.getSalves());
        }else{
            return ResponseUtil.constructNoUserResponse();
        }
    }

    /**
     * 返回负载均衡策略
     * @param account
     * @return
     */
    @RequestMapping(value = "getblance")
    @ResponseBody
    public JSONObject getBlance(@RequestParam(value = "account", required = false)String account,
                                HttpServletRequest request){
        String ip = HttpUtil.getIp(request);
        boolean login = userServie.hasUser(account, ip, 1);
        if(login) {
            return ResponseUtil.constructResponse(ResponseEnum.OK, "success", BaseConfig.BALANCEFUN);
        }else{
            return ResponseUtil.constructNoUserResponse();
        }
    }

    /**
     * 修改权重
     * @param iphash6
     * @param quan
     * @param account
     * @param request
     * @return
     */
    @RequestMapping(value = "changeQuan")
    @ResponseBody
    public JSONObject changeQuan(@RequestParam(value = "iphash6")String iphash6,
                                 @RequestParam(value = "quan")int quan,
                                 @RequestParam(value = "account", required = false) String account,
                                 HttpServletRequest request){
        System.out.println(iphash6 + "  " + quan);
        if(iphash6.length() != 6){
            return ResponseUtil.constructArgErrorResponse("iphash6值不对");
        }
        if(quan > 10 || quan < 1){
            return ResponseUtil.constructArgErrorResponse("权重错误");
        }

        if(SlaveMess.hasSlaveByIp(iphash6) == false){
            return ResponseUtil.constructArgErrorResponse("iphash6不对");
        }

        String ip = HttpUtil.getIp(request);
        boolean login = userServie.hasUser(account, ip, 1);
        if(login == false){
            return ResponseUtil.constructNoUserResponse();
        }

        int result = slaveServie.changeQuan(quan, iphash6);
        if(result == 1){
            return ResponseUtil.constructOKResponse("success", null);
        }else{
            return ResponseUtil.constructUnknownErrorResponse("system error");
        }


    }
}


