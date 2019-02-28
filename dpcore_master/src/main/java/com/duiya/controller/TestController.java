package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.utils.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


@Controller
@RequestMapping("test")
@CrossOrigin
public class TestController {


    @RequestMapping(value = "testUp", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject test(@RequestParam("file") MultipartFile[] multipartFiles,
                           @RequestParam(value = "name", required = false) String name,
                           HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getRequestURI());
        System.out.println(request.getQueryString());
        System.out.println(request.getContextPath());

        System.out.println();


        // 判断文件是否为空
        if (multipartFiles != null && multipartFiles.length >= 1) {
            System.out.println(name);
            return ResponseUtil.constructOKResponse("", "");
        } else {
            return ResponseUtil.constructArgErrorResponse("the file is empty");
        }
    }

    @RequestMapping(value = "testGet", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject testGet(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "address", required = false) String address,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        Enumeration<String> header = request.getHeaderNames();
        System.out.println("==============================");
        System.out.println("============header============");
        while (header.hasMoreElements()) {
            String mm = header.nextElement();
            String val = request.getHeader(mm);
            System.out.println(mm + ": " + val);
        }
        System.out.println("==============body==============");
        System.out.println("method: " + request.getMethod());
        System.out.println("uri: " + request.getRequestURI());
        System.out.println("param: " + request.getQueryString());
        System.out.println("name: " +  name);
        System.out.println("address: " + address);
        return ResponseUtil.constructOKResponse("", "");


    }

    @RequestMapping(value = "testPost", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject testPost(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "address", required = false) String address,
                               HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> header = request.getHeaderNames();
        System.out.println("==============================");
        System.out.println("============header============");
        while (header.hasMoreElements()) {
            String mm = header.nextElement();
            String val = request.getHeader(mm);
            System.out.println(mm + ": " + val);
        }
        System.out.println("==============body==============");
        System.out.println("method: " + request.getMethod());
        System.out.println("uri: " + request.getRequestURI());
        System.out.println("param: " + request.getQueryString());
        System.out.println("name: " +  name);
        System.out.println("address: " + address);
        return ResponseUtil.constructOKResponse("", "");
    }
}
