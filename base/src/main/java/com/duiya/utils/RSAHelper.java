package com.duiya.utils;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class RSAHelper {
    /**
     * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
     */
    public static final int KEY_SIZE = 1024;

    /**
     * 生成公钥、私钥对(keysize=1024)
     */
    public static RSAHelper.KeyPairInfo getKeyPair() {
        return getKeyPair(KEY_SIZE);
    }

    /**
     * 生成公钥、私钥对
     *
     * @param keySize
     * @return
     */
    public static RSAHelper.KeyPairInfo getKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize);
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey oraprivateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey orapublicKey = (RSAPublicKey) keyPair.getPublic();

            RSAHelper.KeyPairInfo pairInfo = new RSAHelper.KeyPairInfo(keySize);
            //公钥
            byte[] publicKeybyte = orapublicKey.getEncoded();
            String publicKeyString = Base64.encode(publicKeybyte);
            pairInfo.setPublicKey(publicKeyString);
            //私钥
            byte[] privateKeybyte = oraprivateKey.getEncoded();
            String privateKeyString = Base64.encode(privateKeybyte);
            pairInfo.setPrivateKey(privateKeyString);

            return pairInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取公钥对象
     *
     * @param publicKeyBase64
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String publicKeyBase64)
            throws InvalidKeySpecException, NoSuchAlgorithmException, Base64DecodingException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicpkcs8KeySpec =
                new X509EncodedKeySpec(Base64.decode(publicKeyBase64));
        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);
        return publicKey;
    }

    /**
     * 获取私钥对象
     *
     * @param privateKeyBase64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64)
            throws NoSuchAlgorithmException, InvalidKeySpecException, Base64DecodingException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privatekcs8KeySpec =
                new PKCS8EncodedKeySpec(Base64.decode(privateKeyBase64));
        PrivateKey privateKey = keyFactory.generatePrivate(privatekcs8KeySpec);
        return privateKey;
    }

    /**
     * 使用工钥加密
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64) {
        return encipher(content, publicKeyBase64, KEY_SIZE / 8 - 11);
    }

    /**
     * 使用公司钥加密（分段加密）
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @param segmentSize     分段大小,一般小于 keySize/8（段小于等于0时，将不使用分段加密）
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64, int segmentSize) {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return encipher(content, publicKey, segmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段加密
     *
     * @param ciphertext  密文
     * @param key         加密秘钥
     * @param segmentSize 分段大小，<=0 不分段
     * @return
     */
    public static String encipher(String ciphertext, java.security.Key key, int segmentSize) {
        try {
            // 用公钥加密
            byte[] srcBytes = ciphertext.getBytes();

            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] resultBytes = null;

            if (segmentSize > 0)
                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize); //分段加密
            else
                resultBytes = cipher.doFinal(srcBytes);
            String base64Str = Base64Utils.encodeToString(resultBytes);
            return base64Str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段大小
     *
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if (segmentSize <= 0)
            throw new RuntimeException("分段大小必须大于0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    /**
     * 使用私钥解密
     *
     * @param contentBase64    待加密内容,base64 编码
     * @param privateKeyBase64 私钥 base64 编码
     * @return
     * @segmentSize 分段大小
     */
    public static String decipher(String contentBase64, String privateKeyBase64) {
        return decipher(contentBase64, privateKeyBase64, KEY_SIZE / 8);
    }

    /**
     * 使用私钥解密（分段解密）
     *
     * @param contentBase64    待加密内容,base64 编码
     * @param privateKeyBase64 私钥 base64 编码
     * @return
     * @segmentSize 分段大小
     */
    public static String decipher(String contentBase64, String privateKeyBase64, int segmentSize) {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return decipher(contentBase64, privateKey, segmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段解密
     *
     * @param contentBase64 密文
     * @param key           解密秘钥
     * @param segmentSize   分段大小（小于等于0不分段）
     * @return
     */
    public static String decipher(String contentBase64, java.security.Key key, int segmentSize) {
        try {
            // 用私钥解密
            byte[] srcBytes = Base64Utils.decodeFromString(contentBase64);
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher deCipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            deCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decBytes = null;//deCipher.doFinal(srcBytes);
            if (segmentSize > 0)
                decBytes = cipherDoFinal(deCipher, srcBytes, segmentSize); //分段加密
            else
                decBytes = deCipher.doFinal(srcBytes);

            String decrytStr = new String(decBytes);
            return decrytStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 秘钥对
     */
    public static class KeyPairInfo {
        String privateKey;
        String publicKey;
        int keySize = 0;

        public KeyPairInfo(int keySize) {
            setKeySize(keySize);
        }

        public KeyPairInfo(String publicKey, String privateKey) {
            setPrivateKey(privateKey);
            setPublicKey(publicKey);
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public int getKeySize() {
            return keySize;
        }

        public void setKeySize(int keySize) {
            this.keySize = keySize;
        }
    }
}
class Temp{
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
     * @param source 明文
     * @param key 密钥
     * @return 加密后的密文
     * @throws Exception
     */
    public static String encrypt(String source, Key key) throws Exception {
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b1);
    }

    /**
     * 解密算法
     * @param cryptograph 密文
     * @param key
     * @return 解密后的明文
     * @throws Exception
     */
    public static String decrypt(String cryptograph, Key key) throws Exception {
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);

        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 将String对象转换为Key对象
     * @param str
     * @return 这个String对应的key
     * @throws Exception
     * @throws IOException
     */
    public static Key getKey(String str) throws Exception, IOException {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(str);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        return key;
    }

    /**
     * 将key转为string
     * @param key
     * @return 这个key对应的String
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
