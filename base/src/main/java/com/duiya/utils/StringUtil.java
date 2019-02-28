package com.duiya.utils;

public class StringUtil {
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

    /**
     * 将int转为长度的string
     * @param value 要转的数字
     * @param length 需要的长度
     * @return
     */
    public static String getLengthString(int value, int length){
        String a = String.valueOf(value);
        StringBuffer sb = new StringBuffer();
        for(int i = a.length(); i < length; i++){
            sb.append(0);
        }
        sb.append(a);
        return sb.toString();

    }
}
