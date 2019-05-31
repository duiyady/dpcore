package com.test.model;

import com.duiya.model.Slave;

public class MessTest {
    public static void main(String[] args) {
        Slave slave1 = new Slave();
        slave1.setBaseUrl("aaa");

        Slave slave2 = new Slave();
        slave2.setBaseUrl("bbb");

        Slave slave3 = new Slave();
        slave3.setBaseUrl("ccc");

        slave2.setBasize(4);
        slave3.setBasize(5);

        SlaveMess.addSlave(slave1);
        show();
        SlaveMess.addSlave(slave2);
        show();
        //SlaveMess.addSlave(slave3);
        show();

        System.out.println(SlaveMess.getFunctionSlave());
        show();

        System.out.println(SlaveMess.getFunctionSlave());
        show();
        System.out.println(SlaveMess.getFunctionSlave());
        show();
        System.out.println(SlaveMess.getFunctionSlave());
        show();



    }

    public static void show(){
        int count = SlaveMess.slaves.size();
        for(int i = 0; i < count; i++){
            Slave slave = SlaveMess.slaves.get(i);
            System.out.println("现在的权重：" + slave.getNowquanz() + "，基础权重：" + slave.getBasize() + "，地址：" + slave.getBaseUrl());
        }
    }
}
