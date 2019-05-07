package com.duiya.model;


import java.util.*;

public class Location {
    private String IPHash6;

    private String YMR;

    private String Min;

    private String UUid8;

    private String Sec;

    private String Hour;

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getFull() {
        return Full;
    }

    public void setFull(String full) {
        Full = full;
    }

    private String Full;

    @Override
    public String toString() {
        return "Location{" +
                "IPHash6='" + IPHash6 + '\'' +
                ", YMR='" + YMR + '\'' +
                ", Min='" + Min + '\'' +
                ", UUid8='" + UUid8 + '\'' +
                ", Sec='" + Sec + '\'' +
                ", Hour='" + Hour + '\'' +
                ", Full='" + Full + '\'' +
                '}';
    }

    public String getIPHash6() {
        return IPHash6;
    }

    public void setIPHash6(String IPHash6) {
        this.IPHash6 = IPHash6;
    }

    public String getYMR() {
        return YMR;
    }

    public void setYMR(String YMR) {
        this.YMR = YMR;
    }

    public String getMin() {
        return Min;
    }

    public void setMin(String min) {
        Min = min;
    }

    public String getUUid8() {
        return UUid8;
    }

    public void setUUid8(String UUid8) {
        this.UUid8 = UUid8;
    }

    public String getSec() {
        return Sec;
    }

    public void setSec(String sec) {
        Sec = sec;
    }

    /**
     * 传入一串图片地址，获取一个Location对象
     * @param string
     * @return
     */
    public static Location getLocation(String string){
        if(string.length() != 28){
            return null;
        }
        Location location = new Location();
        location.setIPHash6(string.substring(0, 6));
        location.setYMR(string.substring(6, 14));
        location.setMin(string.substring(14, 16));
        location.setHour(string.substring(16, 18));
        location.setSec(string.substring(18, 20));
        location.setUUid8(string.substring(20, 28));
        location.setFull(string);
        return location;
    }

    /**
     * 获取location对象在服务器的地址
     * @return
     */
    public String getPath(String ROOT){
        StringBuilder sb = new StringBuilder();
        sb.append(ROOT).append(this.IPHash6).append("/")
                .append(this.YMR).append("/").append(this.Min).append("/").append(this.Hour).append(this.Sec).append(this.UUid8);
        return sb.toString();
    }

    /**
     * 根据图片的名字获取在物理机的地址
     * @param string
     * @return
     */
    public static String getPath(String string, String ROOT){
        StringBuilder sb = new StringBuilder();
        sb.append(ROOT).append(string.substring(0, 6)).append("/")
        .append(string.substring(6, 14)).append("/").append(string.substring(14, 16))
        .append("/").append(string.substring(16, 28));
        return sb.toString();
    }

    public String getPathNoName(String ROOT){
        StringBuilder sb = new StringBuilder();
        sb.append(ROOT).append(this.IPHash6).append("/")
                .append(this.YMR).append("/").append(this.Min);
        return sb.toString();
    }

    public static List<String> getPath(List<String> strings, String ROOT){
        List<String> result = new ArrayList<>();
        for(String s : strings){
            result.add(getPath(s, ROOT));
        }
        return result;
    }

    public static Map<String, String> getFileMess(List<String> strings, String ROOT){
        Map<String, String> res = new HashMap<>();
        for(String s : strings){
            res.put(s, getPath(s, ROOT));
        }
        return res;
    }

    /**
     * 生成文件名字及存路径和时间
     * iphash6/时间(xxxxyyzz格式)/分/时秒UUID前8位
     * @return
     */
    public static List<Object> getFileName(String IPHASH6, String ROOT){
        StringBuilder sbname = new StringBuilder(28);
        StringBuilder sbpath = new StringBuilder(64);
        Calendar now = Calendar.getInstance();
        UUID uuid = UUID.randomUUID();
        String uu = uuid.toString().substring(0,8);
        sbname.append(IPHASH6).append(now.get(Calendar.YEAR));
        sbpath.append(ROOT);
        sbpath.append(IPHASH6).append("/").append(now.get(Calendar.YEAR));
        int month = now.get(Calendar.MONTH) + 1;
        if(month < 10){
            sbname.append('0');
            sbpath.append('0');
        }
        sbname.append(month);
        sbpath.append(month);
        int day = now.get(Calendar.DAY_OF_MONTH);
        if(day < 10){
            sbname.append('0');
            sbpath.append('0');
        }
        sbname.append(day);
        sbpath.append(day);
        sbpath.append("/");
        int minutes = now.get(Calendar.MINUTE);
        if(minutes < 10){
            sbname.append('0');
            sbpath.append('0');
        }
        sbname.append(minutes);
        sbpath.append(minutes);
        sbpath.append("/");

        int hour = now.get(Calendar.HOUR_OF_DAY);
        if(hour < 10){
            sbname.append('0');
            sbpath.append('0');
        }
        sbname.append(hour);
        sbpath.append(hour);

        int sec = now.get(Calendar.SECOND);
        if(sec < 10){
            sbname.append('0');
            sbpath.append('0');
        }
        sbname.append(sec);
        sbpath.append(sec);

        sbname.append(uu);
        sbpath.append(uu);

        List<Object> list = new ArrayList<>();
        list.add(sbname.toString());
        list.add(sbpath.toString());
        list.add(String.valueOf(now.getTimeInMillis()));
        return list;
    }
}
