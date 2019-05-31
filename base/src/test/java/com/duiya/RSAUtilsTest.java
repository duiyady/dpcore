package com.duiya;

import com.duiya.utils.RSAUtil;

import java.security.Key;
import java.util.List;

public class RSAUtilsTest {

    public static void main(String[] args) throws Exception {
        List<Key> keyList = RSAUtil.genKeyPair();

        String pubkey = RSAUtil.getString(keyList.get(0));
        String prikey = RSAUtil.getString(keyList.get(1));
        System.out.println(pubkey.length());
        System.out.println();
        System.out.println("-----------------");
        System.out.println();
        //System.out.println(prikey);
    }
}
