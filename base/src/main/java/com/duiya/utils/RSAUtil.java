package com.duiya.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
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
     * @param source 原始数据
     * @param keyString key的String
     * @return
     */
    public static String encrypt(String source, String keyString) {
        Key key = null;
        try {
            key = getKey(keyString);
        } catch (Exception e) {
            return null;
        }
        return encrypt(source, key);
    }

    /**
     * 加密算法
     * @param source 原始数据
     * @param key
     * @return
     */
    public static String encrypt(String source, Key key) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = new byte[0];
        try {
            b = decoder.decodeBuffer(source);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        byte[] temp = new byte[117];
        try {
            while (((index + 1) * 117) <= b.length) {
                for (int i = 0; i < 117; i++) {
                    temp[i] = b[index * 117 + i];
                }
                sb.append(encrypt(temp, key));
                index++;
            }
            byte[] bb = new byte[b.length % 117];
            for (int i = 0; 117 * index + i < b.length; i++) {
                bb[i] = b[index * 117 + i];
            }
            sb.append(encrypt(bb, key));
        }catch (Exception e){
            return null;
        }
        return sb.toString();
    }

    private static String encrypt(byte[] mess, Key key) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = cipher.doFinal(mess);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);
    }

    public static String decrypt(String cryptograph, String keyString){
        Key key = null;
        try {
            key = getKey(keyString);
        } catch (Exception e) {
            return null;
        }
        return decrypt(cryptograph, key);
    }

    public static String decrypt(String cryptograph, Key key) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b;
        try {
            b = decoder.decodeBuffer(cryptograph);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder sb = new StringBuilder();
        byte[] temp = new byte[128];
        int index = 0;
        try {
            while (((index + 1) * 128) <= b.length) {
                for (int i = 0; i < 128; i++) {
                    temp[i] = b[index * 128 + i];
                }
                sb.append(decrypt(temp, key));
                index++;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    private static String decrypt(byte[] mess, Key key) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(mess);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);
    }


    /**
     * 将String换成key
     * @param str
     * @return
     * @throws Exception
     * @throws IOException
     */
    public static Key getKey(String str) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(str);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        return key;
    }

    /**
     * 将key换成String
     * @param key
     * @return
     */
    public static String getString(Key key){
        byte[] keyBytes = key.getEncoded();
        String keyBase64 = new BASE64Encoder().encode(keyBytes);
        return keyBase64;
    }

    /**
     * 生成公钥和私钥
     * @return list第一个是公钥，第二个是私钥
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
