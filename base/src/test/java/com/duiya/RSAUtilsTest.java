package com.duiya;

import com.duiya.utils.RSAUtil;

import java.security.Key;
import java.util.List;

public class RSAUtilsTest {

    public static void main(String[] args) throws Exception {
        List<Key> list1 = RSAUtil.genKeyPair();
        List<Key> list2 = RSAUtil.genKeyPair();

        String mess = "duiya";
        String mi1 = RSAUtil.encrypt(mess, list1.get(1));
        System.out.println(mi1);
        System.out.println(mi1.getBytes().length);
        System.out.println("===========================");
        String mi2 = RSAUtil.encrypt(mi1, list2.get(0));
        System.out.println(mi2);
        System.out.println(mi2.getBytes().length);
        System.out.println("===========================");

        System.out.println("----------------------------");
        String min2 = RSAUtil.decrypt(mi2, list2.get(1));
        System.out.println(min2);
        System.out.println(min2.getBytes().length);
        System.out.println("===========================");
        String min1 = RSAUtil.decrypt(min2, list1.get(0));
        System.out.println(min1);
        System.out.println(min1.getBytes().length);
        System.out.println("===========================");







//        RSAHelper.KeyPairInfo keyPairInfo1 = RSAHelper.getKeyPair(1024);
//        RSAHelper.KeyPairInfo keyPairInfo2 = RSAHelper.getKeyPair(1024);
//
//        String mess = "duiya";
//        String mi1 = RSAHelper.encipher(mess, keyPairInfo1.getPrivateKey());
//        String mi2 = RSAHelper.encipher(mi1, keyPairInfo2.getPublicKey());
//
//        System.out.println(mi1);
//        System.out.println("==================");
//        System.out.println(mi2);
//
//        System.out.println();
//        System.out.println("---------------------");
//
//        String min2 = RSAHelper.decipher(mi2, keyPairInfo2.getPrivateKey());
//        String min1 = RSAHelper.decipher(min2, keyPairInfo1.getPublicKey());
//        System.out.println(min2);
//        System.out.println("==================");
//        System.out.println(min1);







    }
}
