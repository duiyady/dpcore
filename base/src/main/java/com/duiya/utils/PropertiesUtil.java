package com.duiya.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private InputStreamReader defaultsetIn;
    private Properties defaultsetProp;

    public PropertiesUtil(String path) {
        try {
            defaultsetProp = new Properties();
            defaultsetIn = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path), "UTF-8");
            defaultsetProp.load(defaultsetIn);
        } catch (Exception e) {
            throw new RuntimeException("配置文件找不到");
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
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        return defaultsetProp.getProperty(key);
    }

    /**
     * 获得默认值
     *
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        return Integer.valueOf(defaultsetProp.getProperty(key));
    }

    /**
     * 获得默认值
     *
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        return Long.valueOf(defaultsetProp.getProperty(key));
    }

    /**
     * 获得默认值
     *
     * @param key
     * @return
     */
    public Boolean getBooleanValue(String key) {
        return Boolean.valueOf(defaultsetProp.getProperty(key));
    }
}
