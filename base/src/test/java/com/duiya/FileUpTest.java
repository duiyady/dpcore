package com.duiya;

import com.duiya.model.ResponseModel;
import com.duiya.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class FileUpTest {

    public static void main(String[] args) {
        String url = "http://127.0.0.1:8080/dpcore_master/test/put";
        Map<String,String> fileMap = new HashMap<>();
        fileMap.put("111", "/Users/duiya/tmp/111.jpeg");
        fileMap.put("222", "/Users/duiya/tmp/222.jpeg");
        fileMap.put("333", "/Users/duiya/tmp/123.jpeg");
        ResponseModel res = HttpUtil.sendPostImage(url, null, fileMap, null);
        System.out.println(res.getCode());
        System.out.println(res.getMsg());


    }
}
