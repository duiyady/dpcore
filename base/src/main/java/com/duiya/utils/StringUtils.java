package com.duiya.utils;

public class StringUtils {
    /**
     * 判断String是否为空
     * @param string
     * @return
     */
    public static boolean isBlank(String... string){
        for(int i = 0; i < string.length; i++){
            if(isBlank(string[i]) == true){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断String是否为空
     * @param string
     * @return
     */
    public static boolean isBlank(String string){
        if(string != null && string.trim().length() > 0){
            return false;
        }else {
            return true;
        }
    }
}
