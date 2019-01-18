package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.utils.ResponseUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("fileop")
@CrossOrigin
public class TestController {


    @RequestMapping(value = "test", method = RequestMethod.POST)
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
            return ResponseUtils.constructOKResponse("", "");
        } else {
            return ResponseUtils.constructArgErrorResponse("the file is empty");
        }
    }
}
