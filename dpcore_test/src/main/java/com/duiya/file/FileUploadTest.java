package com.duiya.file;

import com.alibaba.fastjson.JSONObject;
import com.duiyy.util.HttpUtil;
import com.duiyy.util.ResponseModel;
import com.duiyy.util.ResponseModelP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileUploadTest {
    public static void main(String[] args){
        String path = "/Users/duiya/tmp/picture";
        String baseurl = "http://123.207.243.135:8080/dpcore_single/file/upload";
        String baseimg = "\t\t<img alt=\"140x140\" height=\"200\" width=\"200\" src=\"http://123.207.243.135:8080/dpcore_single/file/get?location=";
        String outfilename = "/Users/duiya/tmp/测试界面/sindex.html";
        String timefile = "/Users/duiya/tmp/测试界面/stime.txt";
        singelTest(path, baseurl, baseimg, outfilename, timefile);


    }

    public static void singelTest(String path, String baseurl, String baseimg, String outfilename, String timefile){
        File file = new File(path);
        File[] files = file.listFiles();
        String baseend = "\" />\n";
        Map<String, String> messMap = new HashMap<>();

        File outfile = new File(outfilename);
        File timew = new File(timefile);
        try {
            timew.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(outfile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter timeout = null;
        try {
            timeout = new BufferedWriter(new FileWriter(timew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.write("<html>\n" +
                    "\t<head>\n" +
                    "\t\t<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">\n" +
                    "\t\t<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->\n" +
                    "\t\t<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">\n" +
                    "\t\t<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->\n" +
                    "\t\t<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>\n" +
                    "\t</head>\n" +
                    "\t<body>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


        messMap.put("account", "duiya");
        for(File filet : files){
            try{
                Map<String, String> fileMess = new HashMap<>();
                fileMess.put(filet.getName(), filet.getAbsolutePath());
                long s = new Date().getTime();
                ResponseModel responseModel = HttpUtil.sendPostImage(baseurl, messMap, fileMess,null);
                long e = new Date().getTime();
                timeout.write("size:" + filet.length() + "b, time:" + (e-s) + "ms, resutl:" + responseModel + "\n");
                System.out.println("size:" + filet.length() + "b, time:" + (e-s) + "ms, resutl:" + responseModel);
                ResponseModelP responseModelP = JSONObject.parseObject(responseModel.toString(), ResponseModelP.class);
                String s1 = responseModelP.getData().get(0);
                out.write(baseimg + s1 + baseend);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        try {
            out.write("\t</body>\n" +
                    "</html>");
            out.flush();
            timeout.flush();
            out.close();
            timeout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
