package com.duiya.init;

import com.duiya.model.Escala;
import com.duiya.model.Slave;

import java.util.ArrayList;
import java.util.List;

public class SlaveMess {

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
    public boolean hasSlave(String ip){
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIP().equals(ip)){
                return true;
            }
        }
        return false;
    }

    public static void addSlave(Slave slave){
        slaves.add(slave);
        slaveList.add(slave.getIP());
        Escala escala = new Escala();
        escala.setFlag(0);
        escala.setIP(slave.getIP());
        slaveEs.add(escala);

    }

    public static Slave getSlave(String slaveIp){
        Slave slave = null;
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIP().equals(slaveIp)){
                slave = slaves.get(i);
            }
        }
        return slave;
    }

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
        flag = -1;
        slaveList.remove(slaveIp);
        for(i = 0; i < slaveEs.size(); i++){
            if(slaveEs.get(i).getIP().equals(slaveIp)){
                break;
            }
        }
        if(flag != -1) {
            slaveEs.remove(i);
        }
    }
}
