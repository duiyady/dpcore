package com.duiya.filter;

import com.alibaba.fastjson.JSONObject;
import com.duiya.model.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MyHttpUtils {
    public static ResponseModel transmitPost(String urlString, HttpServletRequest request) {
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            //不是url错误；
            return null;
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", request.getHeader("content-type"));
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(request.getInputStream());
            byte[] temp = new byte[1024];
            int index = 0;
            while ((index = dataInputStream.read(temp)) != -1) {
                dataOutputStream.write(temp, 0, index);
            }
            dataOutputStream.flush();
            dataOutputStream.close();
            dataInputStream.close();

            String strLine = "";
            InputStream in = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            while ((strLine = reader.readLine()) != null) {
                sb.append(strLine);
            }
            return JSONObject.parseObject(sb.toString(), ResponseModel.class);
        } catch (IOException e) {
            return null;//没有打开连接服务器故障了；
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
