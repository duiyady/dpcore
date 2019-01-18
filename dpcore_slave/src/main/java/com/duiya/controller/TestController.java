package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.utils.ResponseUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

@Controller
@RequestMapping("test")
public class TestController {

    @RequestMapping(value = "testGet", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject testGet(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("--------------------------------------");
//        System.out.println("requestUri: " + request.getRequestURI());
//        System.out.println("requestUrl: " + request.getRequestURL());
//        System.out.println("contentType: " + request.getContentType());
//        System.out.println("authtype: " + request.getAuthType());
//        System.out.println("method: " + request.getMethod());
//        Enumeration em = request.getParameterNames();
//        while (em.hasMoreElements()) {
//            String name = (String) em.nextElement();
//            String value = request.getParameter(name);
//            System.out.println(name + ": " + value);
//        }

        Enumeration names = request.getHeaderNames();
        System.out.println("===================================================================");
        while(names.hasMoreElements()){
            String name = (String) names.nextElement();
            System.out.println(name + ":" + request.getHeader(name));
        }
        System.out.println("===================================================================");
        return ResponseUtils.constructOKResponse("","");

    }

    @RequestMapping(value = "testPost", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject testPost(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("--------------------------------------");
//        System.out.println("requestUri: " + request.getRequestURI());
//        System.out.println("requestUrl: " + request.getRequestURL());
//        System.out.println("contentType: " + request.getContentType());
//        System.out.println("authtype: " + request.getAuthType());
//        System.out.println("method: " + request.getMethod());
//        Enumeration em = request.getParameterNames();
//        while (em.hasMoreElements()) {
//            String name = (String) em.nextElement();
//            String value = request.getParameter(name);
//            System.out.println(name + ": " + value);
//        }

        Enumeration names = request.getHeaderNames();
        System.out.println("===================================================================");
        while(names.hasMoreElements()){
            String name = (String) names.nextElement();
            System.out.println(name + ":" + request.getHeader(name));
        }
        System.out.println("===================================================================");



        return ResponseUtils.constructOKResponse("","");
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject fileUpLoad(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        System.out.println("--------------------------------------");
//        System.out.println("requestUri: " + request.getRequestURI());
//        System.out.println("requestUrl: " + request.getRequestURL());
//        System.out.println("contentType: " + request.getContentType());
//        System.out.println("authtype: " + request.getAuthType());
//        System.out.println("method: " + request.getMethod());
//        Enumeration em = request.getParameterNames();
//        while (em.hasMoreElements()) {
//            String name = (String) em.nextElement();
//            String value = request.getParameter(name);
//            System.out.println(name + ": " + value);
//        }

        BufferedReader br = request.getReader();

        String str, wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
        System.out.println(wholeStr);

        return ResponseUtils.constructOKResponse("","");
    }


}
