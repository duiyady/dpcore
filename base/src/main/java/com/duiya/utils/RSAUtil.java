package com.duiya.utils;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
     * 加密
     * @param mess
     * @param key
     * @return
     */
    public static String encrypt(String mess, Key key){
        try {
            // 用公钥加密
            byte[] srcBytes = mess.getBytes();

            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] resultBytes = null;
            resultBytes = encrypt(cipher, srcBytes); //分段加密
            String base64Str = Base64Utils.encodeToString(resultBytes);
            return base64Str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段加密
     * @param cipher
     * @param srcBytes
     * @return
     */
    private static byte[] encrypt(Cipher cipher, byte[] srcBytes)
            throws BadPaddingException, IllegalBlockSizeException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(srcBytes, offSet, 117);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 117;
        }
        byte[] data = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 解密
     * @param mess
     * @param key
     * @return
     */
    public static String decrypt(String mess, Key key){
        try {
            // 用私钥解密
            byte[] srcBytes = Base64Utils.decodeFromString(mess);
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher deCipher = Cipher.getInstance(ALGORITHM);
            // 根据公钥，对Cipher对象进行初始化
            deCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decBytes = null;//deCipher.doFinal(srcBytes);
            decBytes = decrypt(deCipher, srcBytes); //分段加密
            String decrytStr = new String(decBytes);
            return decrytStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段解密
     * @param cipher
     * @param srcBytes
     * @return
     */
    private static byte[] decrypt(Cipher cipher, byte[] srcBytes)
            throws BadPaddingException, IllegalBlockSizeException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(srcBytes, offSet, 128);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] data = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 获取公钥和私钥
     * @return 0是公钥 1是私钥
     * @throws NoSuchAlgorithmException
     */
    public static List<Key> genKeyPair() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEYSIZE, secureRandom);
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

    /**
     * 获取公钥
     * @param publicKeyString
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Base64DecodingException
     */
    public static Key getPublicKey(String publicKeyString)
            throws NoSuchAlgorithmException, InvalidKeySpecException, Base64DecodingException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec publicpkcs8KeySpec = new X509EncodedKeySpec(Base64.decode(publicKeyString));
        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);
        return publicKey;
    }

    /**
     * 获取私钥
     * @param privateKeyString
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Base64DecodingException
     */
    public static Key getPrivateKey(String privateKeyString)
            throws NoSuchAlgorithmException, InvalidKeySpecException, Base64DecodingException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec privatekcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyString));
        PrivateKey privateKey = keyFactory.generatePrivate(privatekcs8KeySpec);
        return privateKey;
    }

    /**
     * 将key变为base64编码String
     * @param key
     * @return
     */
    public static String getString(Key key){
        byte[] keyByte = key.getEncoded();
        String keyString = Base64.encode(keyByte);
        return keyString;
    }
}
