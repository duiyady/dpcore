package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.utils.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;


@Controller
@RequestMapping("test")
@CrossOrigin
public class TestController {

    /**
     * 其他服务器向这个服务器传文件
     * @param multipartFiles
     * @param request
     * @return
     */
    @RequestMapping(value = "put", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin//跨域
    public JSONObject filePut(@RequestParam("file") MultipartFile[] multipartFiles,
                              HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getRemoteAddr();
        //if (slaveService.hasIp(ip)) {
        // 判断文件是否为空
        if (multipartFiles != null && multipartFiles.length >= 1) {
            try {
                System.out.println("hhhhhhh------>" + multipartFiles.length);
                for (int i = 0; i < multipartFiles.length; i++) {
                    MultipartFile mf = multipartFiles[i];
                    if (!mf.isEmpty()) {
                        String contentType = mf.getContentType();
                        System.out.println(contentType.toString());
                        System.out.println(mf.getOriginalFilename());
                        String imageName = mf.getOriginalFilename();
                        System.out.println(imageName);
                        String filePath = "/Users/duiya/temp/" + imageName;
                        System.out.println(filePath);
                        try {
                            File ft = new File(filePath);
                            if (!ft.exists()) {
                                ft.mkdirs();
                            }
                            mf.transferTo(ft);
                        } catch (IOException e) {
                            e.printStackTrace();
                            //logger.error("保存图片失败", e);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //logger.error("failed to upload file", e);
                return ResponseUtil.constructUnknownErrorResponse("unknown error, please try again later");
            }
        } else {
            return ResponseUtil.constructArgErrorResponse("the file is empty");
        }
        return ResponseUtil.constructOKResponse(null, null);
    }

    /**
     * 其他服务器向这个服务器传文件
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "ppp", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin//跨域
    public JSONObject ppp(HttpServletResponse response, HttpServletRequest request) {
//        Enumeration<String> hea = request.getHeaderNames();
//        while(hea.hasMoreElements()){
//            String name = hea.nextElement();
//            String val = request.getHeader(name);
//            System.out.println(name + ": " + val);
//        }
        try {
            System.out.println("获取输入流");
            BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String lin;
            while ((lin = bf.readLine()) != null) {
                sb.append(lin);
            }
            System.out.println(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseUtil.constructOKResponse("ok", null);
    }

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

    @RequestMapping(value = "testPost", method = RequestMethod.POST)
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
