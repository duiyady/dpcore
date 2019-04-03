package com.duiya.init;

import com.duiya.cache.RedisCache;
import com.duiya.model.Escala;
import com.duiya.model.Slave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SlaveMess {
    @Autowired
    private static RedisCache redisCache;

    public static List<Slave> slaves = new ArrayList<>();

    /**
     * 轮寻时用这个
     */
    public static List<String> slaveList = new ArrayList<>();

    private static int index = 0;

    /**
     * 权重时用这个
     */
    private static List<Escala> slaveEs = new ArrayList<>();

    /**
     * 是否有这个slave
     * @param ip
     * @return
     */
    public static boolean hasSlave(String ip){
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIP().equals(ip)){
                return true;
            }
        }
        return false;
    }

    /**
     * 添加slave
     * @param slave
     */
    public static void addSlave(Slave slave){
        Slave temp = getSlave(slave.getIP());
        if(temp == null){
            slaves.add(slave);
        }else{
            temp.setState(1);
            temp.setBaseUrl(slave.getBaseUrl());
            temp.setPublicKey(slave.getPublicKey());
            temp.setIPHash6(slave.getIPHash6());
        }
        redisCache.putPerpetualListCache("slaves", SlaveMess.slaves);
    }

    /**
     * 获取slave
     * @param slaveIp
     * @return
     */
    public static Slave getSlave(String slaveIp){
        Slave slave = null;
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIP().equals(slaveIp)){
                slave = slaves.get(i);
                break;
            }
        }
        return slave;
    }


    /**
     * 删除slave
     * @param slaveIp
     */
    public static void delSlave(String slaveIp){
        int i = 0;
        int flag = -1;
        for(i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIP().equals(slaveIp)){
                flag = i;
                break;
            }
        }
        if(flag != -1){
            slaves.remove(flag);
        }
        redisCache.putPerpetualListCache("slaves", SlaveMess.slaves);
    }
}
