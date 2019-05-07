package com.test.model;

import com.duiya.model.Escala;
import com.duiya.model.Slave;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SlaveMess {

    public static List<Slave> slaves = new ArrayList<>();

    /**
     * 轮训的时候用这个代表第几个
     */
    private static int index = 0;

    /**
     * 权重时用这个
     */
    private static List<Escala> slaveEs = new ArrayList<>();

    /**
     * 是否有这个slave
     * @param baseUrl
     * @return
     */
    public static boolean hasSlave(String baseUrl){
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getBaseUrl().equals(baseUrl)){
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
        Slave temp = getSlave(slave.getBaseUrl());
        if(temp == null){
            slaves.add(slave);
        }else{
            temp.setState(1);
            temp.setBaseUrl(slave.getBaseUrl());
            temp.setPublicKey(slave.getPublicKey());
            temp.setIPHash6(slave.getIPHash6());
        }
        //redisCache.putPerpetualListCache("slaves", SlaveMess.slaves);
    }

    /**
     * 获取slave
     * @param baseUrl
     * @return
     */
    public static Slave getSlave(String baseUrl){
        Slave slave = null;
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getBaseUrl().equals(baseUrl)){
                slave = slaves.get(i);
                break;
            }
        }
        return slave;
    }


    /**
     * 删除slave
     * @param baseUrl
     */
    public static void delSlave(String baseUrl){
        int i = 0;
        int flag = -1;
        for(i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getBaseUrl().equals(baseUrl)){
                flag = i;
                break;
            }
        }
        if(flag != -1){
            slaves.remove(flag);
        }
        //redisCache.putPerpetualListCache("slaves", SlaveMess.slaves);
    }

    public static synchronized String getFunctionSlave(){
        Slave slave = null;
        int count = slaves.size();
        for(Slave slave1 : slaves){
            System.out.println(slave1.toString());
        }
        while(slave == null && count > 0){
            if(index >= slaves.size()){
                index = 0;
            }
            slave = slaves.get(index);
            if(!(slave.getState() == 1)){
                slave = null;
            }
            index++;
            count--;
        }
        if(slave != null){
            return slave.getBaseUrl();
        }else{
            return null;
        }
    }
}
