package com.duiya.init;

import com.duiya.model.Escala;
import com.duiya.model.Slave;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<Map<String, String>> getSalves(){
        List<Map<String, String>> list = new ArrayList<>();
        for(Slave slave : slaves){
            Map<String, String> map = new HashMap();
            map.put("baseurl", slave.getBaseUrl().substring(7));
            map.put("iphash6", slave.getIPHash6());
            map.put("state", String.valueOf(slave.getState()));
            map.put("weight", String.valueOf(slave.getBasize()));
            list.add(map);
        }
        return list;
    }

    /**
     * 通过iphash6判断是否有此slave
     * @param iphash6
     * @return
     */
    public static boolean hasSlaveByIp(String iphash6){
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIPHash6().equals(iphash6)){
                return true;
            }
        }
        return false;
    }

    /**
     * 添加slave
     * @param slave
     */
    public synchronized static void addSlave(Slave slave){
        Slave temp = getSlave(slave.getBaseUrl());
        if(temp == null){
            if(slaves.size() != 0){
                slave.setNowquanz(slaves.get(0).getNowquanz());
            }
            slaves.add(0, slave);
        }else{
            temp.setState(1);
            temp.setBaseUrl(slave.getBaseUrl());
            temp.setPublicKey(slave.getPublicKey());
            temp.setIPHash6(slave.getIPHash6());
            if(slaves.size() != 0){
                slave.setNowquanz(slaves.get(0).getNowquanz());
            }
            changeList();
        }
        try {
            BaseConfig.redisConnection.setList("slaves", SlaveMess.slaves);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取slave
     * @param baseUrl
     * @return
     */
    private static Slave getSlave(String baseUrl){
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
     * 通过iphash6获取slave
     * @param iphash6
     * @return
     */
    private static Slave getSlaveByIP(String iphash6){
        Slave slave = null;
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIPHash6().equals(iphash6)){
                slave = slaves.get(i);
                break;
            }
        }
        return slave;
    }

    public synchronized static int changestate(int state, String iphash6){
        Slave slave = SlaveMess.getSlaveByIP(iphash6);
        if(slave != null){
            slave.setState(state);
            if(BaseConfig.BALANCEFUN == 1){//权值
                SlaveMess.initList();
            }
            return 1;
        }else{
            return 0;
        }
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
        try {
            BaseConfig.redisConnection.setList("slaves", SlaveMess.slaves);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 权重时修改链表
     */
    private static synchronized void changeList(){
        int count = slaves.size();
        for(int i = count-1; i > 0; i--){
            for(int j = 0; j < i; j++){
                if((slaves.get(j).getState() == 1 && slaves.get(j+1).getState() != 1)
                        || (slaves.get(j+1).getNowquanz() < slaves.get(j).getNowquanz() && slaves.get(j+1).getState() ==1 && slaves.get(j).getState() == 1)){
                    Slave slave = slaves.get(j+1);
                    slaves.set(j+1, slaves.get(j));
                    slaves.set(j, slave);
                }
            }
        }
    }

    /**
     * 负载均衡策略为权重时初试化
     */
    public static void initList(){
        int count = slaves.size();
        for(int i = 0; i < count; i++){
            slaves.get(i).setNowquanz(0);
        }
        changeList();
    }

    public static synchronized String getFunctionSlave(){
        if(BaseConfig.BALANCEFUN == 0) {
            Slave slave = null;
            int count = slaves.size();

            while (slave == null && count > 0) {
                if (index >= slaves.size()) {
                    index = 0;
                }
                slave = slaves.get(index);
                if (!(slave.getState() == 1)) {
                    slave = null;
                }
                index++;
                count--;
            }
            if (slave != null) {
                return slave.getBaseUrl();
            } else {
                return null;
            }
        }else{
            if(slaves.size() == 0){
                return null;
            }
            int count = slaves.size();
            Slave slave = null;
            for(int i = 0; i < count; i++){
                slave = slaves.get(i);
                if(slave.getState() != 1){
                    slave = null;
                }else{
                    slave.addNowqz();
                    changeList();
                    break;
                }
            }

            for(Slave slave3 : slaves){
                System.out.println(slave3.getBaseUrl() + " " + slave3.getState() + " " + slave3.getNowquanz() + " " + slave3.getBasize());
            }

            if(slave == null){
                return null;
            }else{
                return slave.getBaseUrl();
            }
        }
    }

    public static synchronized int changeQuan(int quan, String iphash6) {
        Slave slave = SlaveMess.getSlaveByIP(iphash6);
        if(slave != null){
            slave.setBasize(quan);
            return 1;
        }else{
            return 0;
        }
    }
}
