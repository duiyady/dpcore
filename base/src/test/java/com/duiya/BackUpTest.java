package com.duiya;

import com.duiya.utils.HttpUtil;

import java.io.*;
import java.net.URLDecoder;

public class BackUpTest {
    public static void main(String[] args) {
        String url = "http://localhost:10086/dpcore_slave/backup/get";
        String param = null;
        try {
            param = "location=" + URLDecoder.decode("15680020190122162245809cffbb", "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {
            in = HttpUtil.sendGetByte(url, param);
            BufferedInputStream bi = new BufferedInputStream(in);
            byte[] code = new byte[3];
            byte[] location = new byte[28];
            byte[] length = new byte[10];
            byte[] temp = new byte[1024];
            int total = bi.available();
            int now = 3;
            bi.read(code);
            if(new String(code).equals("300")){
                while(now < total){
                    bi.read(location);
                    String loca = new String(location);
                    bi.read(length);
                    int nlen = Integer.valueOf(new String(length));
                    System.out.println(nlen);
                    int i = 1;
                    int len = 0;
                    int ttt = 0;
                    FileOutputStream fout = new FileOutputStream(new File("/Users/duiya/tests.png"));
                    while(i*1024 < nlen){
                        bi.read(temp);
                        fout.write(temp);
                        i++;
                        ttt += 1024;
                    }
                    bi.read(temp, 0, nlen-1024*(i-1));
                    fout.write(temp, 0, nlen-1024*(i-1));
                    ttt+= nlen-1024*(i-1);
                    now += nlen;
//                    while((len = bi.read(temp)) != -1){
//                        ttt += len;
//                        fout.write(temp, 0, len);
//                    }
//                    now  = Integer.MAX_VALUE;
                    System.out.println(ttt);
                    fout.flush();
                    fout.close();
                }
            }
            bi.close();
        }catch (Exception e){

        }

    }
}
