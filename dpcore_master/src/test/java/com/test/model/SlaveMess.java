package com.test.model;

import com.duiya.model.Slave;

import java.util.ArrayList;
import java.util.List;

public class SlaveMess {
    public static List<Slave> slaves = new ArrayList<>();

    /**
     * 轮训的时候用这个代表第几个
     */
    private static int index = 0;


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
    public static void addSlave(Slave slave){
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * 通过iphash6获取slave
     * @param iphash6
     * @return
     */
    public static Slave getSlaveByIP(String iphash6){
        Slave slave = null;
        for(int i = 0; i < slaves.size(); i++){
            if(slaves.get(i).getIPHash6().equals(iphash6)){
                slave = slaves.get(i);
                break;
            }
        }
        return slave;
    }

    /**
     * 使用权重负载均衡时，slave更新后修改状态
     */
    public static synchronized void createList(){
        int i = 0;
        for(int k = 0; k < slaves.size(); i++){
            if(slaves.get(k).getState() == 2){
                if(i == k){

                }else{
                    Slave slave1 = slaves.get(k);
                    Slave slave2 = slaves.get(i);
                    slaves.set(k, slave2);
                    slaves.set(i, slave1);
                }
                i++;
            }
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
            System.out.println(i);
            for(int j = 0; j < i; j++){
                System.out.println(j);
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
     * 初始化链表状态
     */
    public static void initList(){
        int count = slaves.size();
        for(int i = 0; i < count; i++){
            slaves.get(i).setNowquanz(0);
        }
        changeList();
    }

    public static synchronized String getFunctionSlave(){

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
            if(slave == null){
                return null;
            }else{
                return slave.getBaseUrl();
            }
    }
}
