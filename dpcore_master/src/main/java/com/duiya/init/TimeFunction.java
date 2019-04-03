package com.duiya.init;

import com.duiya.model.ResponseModel;
import com.duiya.model.Slave;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 定时发送看是否还存活
 */
//@Service
public class TimeFunction {
    private Logger logger = LoggerFactory.getLogger(TimeFunction.class);

    /**
     * 每个30分钟同步数据
     */
    @Scheduled(fixedRate = 1000 * 60 * 30) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行。
    public void requestASYN() {
        Long now = new Date().getTime();
        BaseConfig.ASYNtime1 = BaseConfig.ASYNtime2;
        BaseConfig.ASYNtime2 = now;

        logger.info("-----------------开始同步----------------");
        String preUrl = "http://";
        String lastUrl = ":10086/dpcore-slave/monitor/sync";
        StringBuilder sb = new StringBuilder();
        //http://ip:8080/dpcore-slave/monitor/alive
        try {
            String s1 = RSAUtil.decrypt(BaseConfig.IPHASH6, BaseConfig.PRIVATE_KEY);
            for(String s : SlaveMess.slaveList){
                Slave slave = SlaveMess.getSlave(s);
                String s2 = RSAUtil.decrypt(s1, slave.getPublicKey());
               // ResponseModel responseModel = HttpUtil.sendPost()

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每隔30秒发送一次心跳
     */
    @Scheduled(fixedRate = 1000 * 30) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行。
    public void isAlive() {
        logger.info("-----------------开始心跳检测----------------");
        List<String> del = new LinkedList<>();
        String preUrl = "http://";
        String lastUrl = ":8080/dpcore-slave/monitor/alive";
        StringBuilder sb = new StringBuilder();
        //http://ip:8080/dpcore-slave/monitor/alive
        for(Slave slave : SlaveMess.slaves){
            String ip = slave.getIP();
            String url = sb.append(preUrl).append(ip).append(lastUrl).toString();
            ResponseModel responseModel = null;
            try {
                responseModel = HttpUtil.sendGetModel(url, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(responseModel == null){
                del.add(ip);
            }else{
                logger.info(ip + "存活...........");
                SlaveMess.getSlave(ip).setState(0);
            }
        }
        for(String s : del){
            Slave slave = SlaveMess.getSlave(s);
            int id = slave.getState();
            if(id == 10){

            }else{
                id++;
                if(id == 3){
                    SlaveMess.delSlave(s);
                    logger.info(s + "故障...........");
                }else{
                    slave.setState(id);
                }
            }
        }
    }


    @Scheduled(cron = "0 34 9 ? * *"  ) //使用cron属性可按照指定时间执行，本例指的是每天9点27分执行；cron是UNIX和类UNIX(Linux)系统下的定时任务。
    public void fixTimeExecution(){
        System.out.println("在指定时间 ");

    }
}
