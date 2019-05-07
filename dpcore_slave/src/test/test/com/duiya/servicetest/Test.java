package com.duiya.servicetest;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        String ss = "127.0.0.1:10096/dpcore_slave";
        String s = String.valueOf(ss.hashCode());
        System.out.println(s);

    }

    public static void test(String a){

    }
}
