package com.duiya.servicetest;

import com.duiya.model.Slave;
import com.duiya.utils.RedisConnection;

import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        RedisConnection redisConnection = new RedisConnection("123.207.243.135", 6379, "duiyady.");
        List<Slave> slaveList = redisConnection.getList("slaves", Slave.class);
        for(Slave slave : slaveList){
            System.out.println(slave.getIPHash6() + "=" + slave.getIP() + "=" + slave.getBaseUrl());

        }

    }

    public static void test(String a){

    }
}
