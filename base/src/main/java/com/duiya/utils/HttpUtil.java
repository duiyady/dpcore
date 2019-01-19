package com.duiya.utils;

import com.alibaba.fastjson.JSONObject;
import com.duiya.model.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 发送get请求返回string
     *
     * @param url 请求地址
     * @param param 参数
     * @return
     * @throws IOException
     */
    public static String sendGet(String url, String param) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            }catch (IOException e){
                logger.error("sendGet--------->", e);
            }
        }
    }

    /**
     * 发送get请求返回ResponseModel
     * @param url 请求地址
     * @param param 参数
     * @return
     * @throws IOException
     */
    public static ResponseModel sendGetModel(String url, String param) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return JSONObject.parseObject(result.toString(), ResponseModel.class);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            }catch (IOException e){
                logger.error("sendGetToGetPic--------->", e);
            }
        }
    }


    /**
     * 发送post请求返回string
     * @param url 请求地址
     * @param param 参数
     * @return
     * @throws IOException
     */
    public static String sendPost(String url, String param) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 发送post请求返回ResponseModel
     * @param url 请求地址
     * @param param 参数
     * @return
     * @throws IOException
     */
    public static ResponseModel sendPostModel(String url, String param) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return JSONObject.parseObject(sb.toString(), ResponseModel.class);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 转发post请求
     * @param urlString 请求地址
     * @param request HttpServletRequest流
     * @return
     * @throws IOException
     */
    public static ResponseModel transmitPost(String urlString, HttpServletRequest request) throws IOException {
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

            String strLine;
            InputStream in = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            while ((strLine = reader.readLine()) != null) {
                sb.append(strLine);
            }
            return JSONObject.parseObject(sb.toString(), ResponseModel.class);
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }


}
