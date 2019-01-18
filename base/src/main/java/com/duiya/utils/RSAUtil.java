package com.duiya.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class RSAUtil {
    /**
     *  指定加密算法为RSA 
     */
    private static final String ALGORITHM = "RSA";
    /**
     *  密钥长度，用来初始化 
     */
    private static final int KEYSIZE = 1024;

    /**
     * 加密算法
     * @param source
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String source, Key privateKey) throws Exception {
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b1);
    }

    /**
     * 解密算法
     * @param cryptograph
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String cryptograph, Key publicKey) throws Exception {

        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);

        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 将String对象转换为Key对象
     * @param str
     * @return
     * @throws Exception
     * @throws IOException
     */
    public static Key getKey(String str) throws Exception, IOException {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(str);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 将key转为string
     * @param key
     * @return
     */
    public static String getString(Key key){
        byte[] publicKeyBytes = key.getEncoded();
        String publicKeyBase64 = new BASE64Encoder().encode(publicKeyBytes);
        return publicKeyBase64;
    }

    /**
     * 生成公钥和私钥
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static List<Key> genKeyPair() throws NoSuchAlgorithmException {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom secureRandom = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        keyPairGenerator.initialize(KEYSIZE, secureRandom);

        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();
        /** 得到私钥 */
        Key privateKey = keyPair.getPrivate();

        List<Key> list = new ArrayList<>();
        list.add(publicKey);
        list.add(privateKey);
        return list;
    }
}
