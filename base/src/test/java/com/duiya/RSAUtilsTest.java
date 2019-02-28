package com.duiya;

import com.duiya.utils.RSAUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Key;

public class RSAUtilsTest {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/duiya/key/masterPubKey"))));
        String line = reader.readLine(); // 读取第一行
        StringBuilder sb = new StringBuilder();
        while (line != null) { // 如果 line 为空说明读完了
            sb.append(line); // 将读到的内容添加到 buffer 中
            sb.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        String masterPubKeyS = sb.toString();
        sb = new StringBuilder();


        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/duiya/key/masterPriKey"))));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            sb.append(line); // 将读到的内容添加到 buffer 中
            sb.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        String masterPriKeyS = sb.toString();
        sb = new StringBuilder();

        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/duiya/key/slavePubKey"))));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            sb.append(line); // 将读到的内容添加到 buffer 中
            sb.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        String slavePubKeyS = sb.toString();
        sb = new StringBuilder();

        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/duiya/key/slavePriKey"))));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            sb.append(line); // 将读到的内容添加到 buffer 中
            sb.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        String slavePriKeyS = sb.toString();
        sb = new StringBuilder();


        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/duiya/key/miwen"))));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            sb.append(line); // 将读到的内容添加到 buffer 中
            sb.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        String miwen = sb.toString();
        sb = new StringBuilder();

        System.out.println(masterPubKeyS);
        System.out.println("===================");
        System.out.println(masterPriKeyS);
        System.out.println("===================");
        System.out.println(slavePubKeyS);
        System.out.println("===================");
        System.out.println(slavePriKeyS);
        System.out.println("===================");
        System.out.println(miwen);

        Key masPubK = RSAUtil.getPublicKey(masterPubKeyS);
        Key masPriK = RSAUtil.getPrivateKey(masterPriKeyS);

        Key slaPubK = RSAUtil.getPublicKey(slavePubKeyS);
        Key slaPriK = RSAUtil.getPrivateKey(slavePriKeyS);

        String mess = "172.33.12.153";
        String a = String.valueOf(mess.hashCode()).substring(0, 6);
        //System.out.println(a);

        String mi1 = RSAUtil.encrypt(a, slaPriK);
        String mi2 = RSAUtil.encrypt(mi1, masPubK);
        //System.out.println(mi2);
        String min2 = RSAUtil.decrypt(mi2, masPriK);
        String min1 = RSAUtil.decrypt(min2, slaPubK);

        min2 = RSAUtil.decrypt(miwen, masPriK);
        min1 = RSAUtil.decrypt(min2, slaPubK);
        System.out.println(min1);
    }
}
