package com.duiya.init;

import com.duiya.cache.RedisCache;
import com.duiya.model.ResponseModel;
import com.duiya.model.ServerCache;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.RSAUtil;
import com.duiya.utils.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;


@Service
public class TimeFunction {

    private Logger logger = LoggerFactory.getLogger(TimeFunction.class);


    @Autowired
    private RedisCache redisCache;

    /**
     * 每隔90秒检测最近同步
     * 通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行。
     */
    @Scheduled(fixedRate = 1000 * 90)
    public void isAlive() {
        long now  = new Date().getTime();
        if((now - BaseConfig.RECENT_TIME) > 1000*90){
            ServerCache master = null;
            try {
                master = redisCache.getCache("master", ServerCache.class);
            } catch (Exception e) {

            }
            if(master != null){
                BaseConfig.MASTER_IP = master.getIP();
                BaseConfig.MASTER_PUBLICKEY = master.getPUBLICKEY();
                BaseConfig.MASTER_URL = master.getBASEURL();
                BaseConfig.MASTER_IPHASH6 = master.getIPHASH6();

                String flag = null;
                try {
                    String iphash6m1 = RSAUtil.encrypt(BaseConfig.IPHASH6, BaseConfig.PRIVATE_KEY);
                    flag = RSAUtil.encrypt(iphash6m1, BaseConfig.MASTER_PUBLICKEY);
                } catch (Exception e) {
                    logger.error("生成register的flag时失败", e);
                }

                String registurl = BaseConfig.MASTER_URL + "/slave/regist";
                String param = null;
                try {
                    param = "flag=" + URLEncoder.encode(flag, "utf8") + "&pubKeyStr=" + URLEncoder.encode(RSAUtil.getString(BaseConfig.PUBLIC_KEY), "utf8") + "&baseUrl=" + URLEncoder.encode(BaseConfig.LOCAL_URL, "utf8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("注册时转码失败", e);
                }

                ResponseModel responseModel = null;
                try {
                    responseModel = HttpUtil.sendPostModel(registurl, param);
                    if (responseModel.getCode() == ResponseEnum.OK) {
                        BaseConfig.RECENT_TIME = new Date().getTime();
                        logger.info("注册成功: ", master.getIP());
                    }
                } catch (IOException e) {
                    logger.error("向主服务器注册失败", e);
                }
            }else{
                logger.info("系统没有mster");
            }
        }
    }

}
