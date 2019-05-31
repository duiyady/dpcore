package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.service.UserService;
import com.duiya.utils.ResponseUtil;
import com.duiya.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("user")
//@CrossOrigin
public class UserController {


    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @RequestMapping(value = "regist", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject regist(@RequestParam(value = "account")String account,
                             @RequestParam(value = "password")String password,
                             @RequestParam(value = "publicKey")String publicKey,
                             @RequestParam(value = "userEmail", required = false)String userEmail){

        if(StringUtil.isBlank(account, password, publicKey)){
            return ResponseUtil.constructArgErrorResponse("参数不能为空");
        }
        if(account.length() > 16){
            return ResponseUtil.constructArgErrorResponse("账户名不能超过16位");
        }
        if(password.length() > 20){
            return ResponseUtil.constructArgErrorResponse("密码不能超过20位");
        }
        int result = -1;
        try {
            result = userService.regist(account, password, publicKey, userEmail);
        }catch (Exception e){
            return ResponseUtil.constructUnknownErrorResponse("系统错误");
        }
        if(result == 1){
            return ResponseUtil.constructArgErrorResponse("账户已经存在");
        }else{
            return ResponseUtil.constructOKResponse("success", null);
        }
    }


    @RequestMapping(value = "login")
    @ResponseBody
    public JSONObject regist(@RequestParam(value = "account")String account,
                             @RequestParam(value = "password")String password,
                             HttpServletRequest request, HttpServletResponse response){

        System.out.println(account + "   " + password);
        if(StringUtil.isBlank(account, password)){
            return ResponseUtil.constructArgErrorResponse("参数不能为空");
        }
        String result = null;
        try {
            result = userService.login(account, password, request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.constructUnknownErrorResponse("系统错误");
        }
        if(result.equals("anull")){
            return ResponseUtil.constructArgErrorResponse("账户不存在");
        }else if(result.equals("epass")){
            return ResponseUtil.constructArgErrorResponse("密码错误");
        }else if(result.equals("error")){
            return ResponseUtil.constructUnknownErrorResponse("系统错误");
        }else if(result.equals("successr")){
            StringBuffer url = request.getRequestURL();
            String str = url.substring(0, url.length()-10);
            //管理员
            return ResponseUtil.constructOKResponse("success",str + "rootindex.html");
        }else{
            //普通用户
            StringBuffer url = request.getRequestURL();
            String str = url.substring(0, url.length()-10);
            return ResponseUtil.constructOKResponse("success",str + "fileshow.html");
        }
    }
}
