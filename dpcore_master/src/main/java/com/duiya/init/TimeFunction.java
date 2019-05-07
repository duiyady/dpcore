package com.duiya.init;

import com.duiya.model.ResponseModel;
import com.duiya.model.Slave;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.RSAUtil;
import com.duiya.utils.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 定时发送看是否还存活
 */
@Service
public class TimeFunction {
    private Logger logger = LoggerFactory.getLogger(TimeFunction.class);

    /**
     * 每个30分钟同步数据
     */
    @Scheduled(fixedRate = 1000 * 60 * 1) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行。
    public void requestASYN() {
        Long now = new Date().getTime();
        BaseConfig.ASYNtime1 = BaseConfig.ASYNtime2;
        BaseConfig.ASYNtime2 = now;

        logger.info("-----------------开始同步----------------");
        String s1 = RSAUtil.encrypt(BaseConfig.IPHASH6, BaseConfig.PRIVATE_KEY);
        String baseparam = "last=" + BaseConfig.ASYNtime1 + "&now=" + BaseConfig.ASYNtime2;
        for(Slave slave : SlaveMess.slaves){
            if(slave.getState() == 10 || slave.getState() == 1){
                String baseurl = slave.getBaseUrl() + "/monitor/sync";
                String s2 = RSAUtil.encrypt(s1, slave.getPublicKey());
                System.out.println(s2);
                String param = null;
                try {
                    param = baseparam + "&flag=" + URLEncoder.encode(s2,"utf8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("转码失败");
                }
                ResponseModel responseModel = null;
                try {
                    responseModel = HttpUtil.sendPostModel(baseurl, param);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(responseModel == null || responseModel.getCode() != ResponseEnum.OK){
                    logger.error("通知同步失败---->"+ slave.getBaseUrl() + ":" + BaseConfig.ASYNtime1 + ">" + BaseConfig.ASYNtime2);
                }else{
                    logger.info("通知同步成功---->"+ slave.getBaseUrl() + ":" + BaseConfig.ASYNtime1 + ">" + BaseConfig.ASYNtime2);
                }
            }
        }

    }

    /**
     * 每隔30秒发送一次心跳
     */
    @Scheduled(fixedRate = 1000 * 30) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行。
    public void isAlive() {
        logger.info("-----------------开始心跳检测----------------");
        List<String> del = new LinkedList<>();
        //http://ip:8080/dpcore-slave/monitor/alive
        for(Slave slave : SlaveMess.slaves){
            System.out.println(slave.getBaseUrl());
            String url = slave.getBaseUrl() + "/monitor/alive";
            String baseUrl = slave.getBaseUrl();
            ResponseModel responseModel = null;
            try {
                responseModel = HttpUtil.sendGetModel(url, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(responseModel == null){
                if(slave.getState() == 10){
                    slave.setState(2);
                }else{
                    slave.setState(slave.getState()+1);
                    if(slave.getState() == 3){
                        del.add(baseUrl);

                    }
                }
            }else{
                logger.info(baseUrl + "存活...........");
                SlaveMess.getSlave(baseUrl).setState(1);
            }
        }
        for(String s : del){
            SlaveMess.delSlave(s);
            logger.info(s + ":故障...........");
        }
    }


    @Scheduled(cron = "0 34 9 ? * *"  ) //使用cron属性可按照指定时间执行，本例指的是每天9点27分执行；cron是UNIX和类UNIX(Linux)系统下的定时任务。
    public void fixTimeExecution(){
        System.out.println("在指定时间 ");

    }
}
