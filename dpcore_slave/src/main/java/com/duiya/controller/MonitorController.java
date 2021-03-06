package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.cache.RedisCache;
import com.duiya.init.BaseConfig;
import com.duiya.model.ServerCache;
import com.duiya.service.BackupService;
import com.duiya.utils.RSAUtil;
import com.duiya.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("monitor")
public class MonitorController {

    private Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    public RedisCache redisCache;

    @Autowired
    public BackupService backupService;

    //控制端检测服务器是否正常
    @RequestMapping(value = "alive", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject alive(){
        logger.info("invoke--------------------monitor/alive");
        BaseConfig.RECENT_TIME = new Date().getTime();
        return ResponseUtil.constructOKResponse("success", null);
    }

    //控制端检测服务器是否正常
    @RequestMapping(value = "updateState", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject updateMaster(){
        logger.info("invoke--------------------monitor/updateState");
        ServerCache master = null;
        try {
            master = redisCache.getCache("master", ServerCache.class);
        }catch (Exception e){
            return ResponseUtil.constructUnknownErrorResponse(null);
        }
        if(master != null){
            BaseConfig.MASTER_IPHASH6 = master.getIPHASH6();
            BaseConfig.MASTER_IP = master.getIP();
            BaseConfig.MASTER_URL = master.getBASEURL();
            BaseConfig.MASTER_PUBLICKEY = master.getPUBLICKEY();
            BaseConfig.RECENT_TIME = new Date().getTime();
            return ResponseUtil.constructOKResponse("success", null);
        }else{
            return ResponseUtil.constructUnknownErrorResponse(null);
        }
    }

    /**
     * 控制端发送数据同步请求
     * @param last 上一次同步时间
     * @param now  这次同步时间
     * @param flag master用自己的私钥加密他的IPHASH6，再用slave的公钥加密
     * @return
     */
    @RequestMapping(value = "sync", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sync(@RequestParam(value = "last") Long last,
                           @RequestParam(value = "now") Long now,
                           @RequestParam(value = "flag") String flag){
        logger.info("invoke--------------------monitor/sync?last:" + last + ",now:" + now);
        String s2 = null;
        try {
            String s1 = RSAUtil.decrypt(flag, BaseConfig.PRIVATE_KEY);
            s2 = RSAUtil.decrypt(s1, BaseConfig.MASTER_PUBLICKEY);
        } catch (Exception e) {
            logger.error("未知错误", e);
            return ResponseUtil.constructUnknownErrorResponse("failed");
        }
        if(s2.equals(BaseConfig.MASTER_IPHASH6)){
            backupService.aync(last, now);
        }
        logger.info("invoke--------------------monitor/sync?last:" + last + ",now:" + now + "success");
        return ResponseUtil.constructOKResponse("success", null);
    }


}
