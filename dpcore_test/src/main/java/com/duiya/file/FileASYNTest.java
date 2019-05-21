package com.duiya.file;

import com.duiyy.util.HttpUtil;
import com.duiyy.util.ResponseModel;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class FileASYNTest {

    public static byte[] PIC = new byte[1024 * 1024 * 40];


    public static void main(String[] args) {
        Map<String, String> fileMap = new HashMap<>();
        File file = new File("/Users/duiya/tmp/picture");
        File[] files = file.listFiles();
        String fff = "12332120190516102021";
        for(int i = 0; i < files.length; i++){
            UUID uuid = UUID.randomUUID();
            String uu = uuid.toString().substring(0,8);
            fileMap.put(fff + uu, files[i].getAbsolutePath());
        }

        List<String> list = new LinkedList<>();
        list.add("http://123.207.243.135:8080/dpcore_slave/backup/put");
        list.add("http://111.231.68.12:10086/dpcore_slave/backup/put");

        //list.add("http://127.0.0.1:10076/dpcore_slave/backup/put");

        try {
            long now = new Date().getTime();
            fileAS(fileMap, list);
            long end = new Date().getTime();
            System.out.println("1:" + (end-now));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long now = new Date().getTime();
        fileAS2(fileMap, list);
        long end = new Date().getTime();
        System.out.println("2:" + (end-now));
    }


    public static void fileAS(Map<String, String> filel, List<String> list) throws Exception {

        String BOUNDARY = "----WebKitFormBoundaryXHSA8cv34gLSI3UH";
        String pres = "\r\n--" + BOUNDARY + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"";

        String ends = "\"\r\nContent-Type:image/png\r\n\r\n";
        byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf8");

        byte[] pre = pres.getBytes("utf8");
        byte[] end = ends.getBytes("utf8");
        byte[] tempbyte = new byte[2048];

        Iterator<Map.Entry<String, String>> iterator = filel.entrySet().iterator();
        int size = 0;
        while (iterator.hasNext()) {
            if(size < 1024*1024*35) {
                Map.Entry<String, String> entry = iterator.next();
                String name = entry.getKey();
                String path = entry.getValue();
                System.arraycopy(pre, 0, PIC, size, pre.length);
                size += pre.length;
                byte[] fn = name.getBytes("utf8");
                System.arraycopy(fn, 0, PIC, size, fn.length);
                size += fn.length;
                System.arraycopy(end, 0, PIC, size, end.length);
                size += end.length;

                DataInputStream in = new DataInputStream(new FileInputStream(path));
                int bytes = 0;
                while ((bytes = in.read(tempbyte)) != -1) {
                    System.arraycopy(tempbyte, 0, PIC, size, bytes);
                    size += bytes;
                }
            }else{
                System.arraycopy(endData, 0, PIC, size, endData.length);
                size += endData.length;
                for(String s : list) {
                    ResponseModel responseModel = HttpUtil.ASYN(PIC, BOUNDARY, size, s);
                    if (responseModel != null) {
                        System.out.println(responseModel.toString());
                    }
                }
                size = 0;
            }
        }
        System.arraycopy(endData, 0, PIC, size, endData.length);
        size += endData.length;
        for(String s : list) {
            ResponseModel responseModel = HttpUtil.ASYN(PIC, BOUNDARY, size, s);
            if (responseModel != null) {
                System.out.println(responseModel.toString());
            }
        }
    }


    public static void fileAS2(Map<String, String> filel, List<String> list){
        Iterator<Map.Entry<String, String>> iterator = filel.entrySet().iterator();
        Map<String, String> temp = new HashMap<>();
        int count = 1;
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if(count%30 == 0){
                for(String urlstr : list){
                    ResponseModel responseModel = HttpUtil.sendPostImage(urlstr, null, temp, null);
                    System.out.println(responseModel.getCode());
                }
                temp = new HashMap<>();
                temp.put(entry.getKey(), entry.getValue());
            }else{
                temp.put(entry.getKey(), entry.getValue());
            }
            count++;
        }
        for(String urlstr : list){
            ResponseModel responseModel = HttpUtil.sendPostImage(urlstr, null, temp, null);
            System.out.println(responseModel.getCode());
        }
    }
}
