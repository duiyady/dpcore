package com.duiya.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
   // private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static InputStreamReader defaultsetIn;
    private static Properties defaultsetProp;

    private PropertiesUtil() {
        // 不能实例化
    }

    static {
        try {
            defaultsetProp = new Properties();
            defaultsetIn = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("dpcore-master.properties"), "UTF-8");
            defaultsetProp.load(defaultsetIn);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //logger.error("failed to read defaultset.properties", e);
        } finally {
            if (defaultsetIn != null) {
                try {
                    defaultsetIn.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得默认设置的值
     * @param key
     * @return
     */
    public static String getStringValue(String key) {
        return defaultsetProp.getProperty(key);
    }

    /**
     * 获得默认值
     * @param key
     * @return
     */
    public static int getIntValue(String key){
        return Integer.valueOf(defaultsetProp.getProperty(key));
    }

    /**
     * 获得默认值
     * @param key
     * @return
     */
    public static long getLongValue(String key){
        return Long.valueOf(defaultsetProp.getProperty(key));
    }

    /**
     * 获得默认值
     * @param key
     * @return
     */
    public static boolean getBooleanValue(String key){
        return Boolean.valueOf(defaultsetProp.getProperty(key));
    }
}
