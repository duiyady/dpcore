package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.init.DPCoreInit;
import com.duiya.model.Location;
import com.duiya.service.FileBackupService;
import com.duiya.service.FileManageService;
import com.duiya.service.KeyService;
import com.duiya.utils.ResponseUtils;
import com.duiya.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件处理对外接口，包含文件上传，文件读取，文件删除
 * 文件上传有两种模式，一种是没有密码验证，一种是需要验证密钥
 * 文件名格式：第一次存储服务器IP的Hash前6位/时间(xxxxyyzz格式)/分/图片名称+秒的hash值
 * 这个接口是暴露给公共访问的
 */
@Controller
@RequestMapping("fileop")
@CrossOrigin
public class FileManageController {

    //logger.info("invoke--------------------mflower/addFlower?flower:"+flower);
    private Logger logger = LoggerFactory.getLogger(FileManageController.class);

    @Autowired
    private FileManageService fileManageService;

    @Autowired
    private KeyService keyService;

    @Autowired
    private FileBackupService fileBackupService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject fileUpLoad(@RequestParam("file") MultipartFile[] multipartFiles,
                                 @RequestParam(value = "account") String account,
                                 @RequestParam(value = "key", required = false) String key){


        if(DPCoreInit.VERIFIED){
            if(StringUtils.isBlank(account, key)){
                return ResponseUtils.constructArgErrorResponse("you should get a key");
            }
        }

        // 判断文件是否为空
        if (multipartFiles != null && multipartFiles.length>=1) {
            try {
                //文件数量过多
                if(multipartFiles.length > 9){
                    return ResponseUtils.constructOversizeResponse("please upload less than 9 files");
                }
                //如果需要判断密钥是否正确
                if(keyService.verify(account, key)) {
                    //进行文件保存
                    List<String> result = fileManageService.saveFile(account, multipartFiles);
                    if (result != null) {
                        return ResponseUtils.constructOKResponse("requst succeed", result);
                    } else {
                        return ResponseUtils.constructUnknownErrorResponse("unknow error, please try again later");
                    }
                }else{
                    return ResponseUtils.constructArgErrorResponse("key error");
                }
            } catch (Exception e) {
                logger.error("failed to upload file", e);
                return ResponseUtils.constructUnknownErrorResponse("unknown error, please try again later");
            }
        }else{
            return ResponseUtils.constructArgErrorResponse("the file is empty");
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public String fileGet(@RequestParam("location") String location, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        Location location1 = Location.getLocation(location);
        //图片地址不符合规范
        if(location1 == null){
            return "the location is error";
        }
        String path = location1.getPath(DPCoreInit.ROOT_LOCATION);
        File file = new File(path);
        byte[] img = null;
        if(!file.exists()){
            //文件不存在而且第一次保存主机是本机，则文件肯定不对
            if(location1.getIPHash6().equals(DPCoreInit.IPHASH6)){
                return "can not find ther pic";
            }else{
                //向文件第一个主机请求同步
                boolean res = fileBackupService.getFile(location1);
                if(res == false){//文件在那个主机也没有，证明文件不存在
                    return "can not find the pic";
                }
            }
        }
        img = fileManageService.getFile(path);
        if(img == null){
           return "unknown error";
        }
        httpServletResponse.setContentType("image/png");
        OutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
            os.write(img);
            os.flush();
            os.close();
            return "success";
        } catch (IOException e) {
            logger.error("返回图片失败", e);
            return "unknown error";
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject fileDelete(@RequestParam(name = "account")String account,
                                 @RequestParam(name = "key") String key,
                                 @RequestParam(name = "location")String location){
        if(!StringUtils.isBlank(account, key, location)){
            //进行删除，要检验那个图片是否是那个用户的

        }else{
            return ResponseUtils.constructArgErrorResponse("the account, key and location cannot be empty");
        }
        return null;
    }

    @RequestMapping(value = "testGet", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject testGet(){
        return ResponseUtils.constructOKResponse("successget", "duiya");
    }

    @RequestMapping(value = "testPost", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject testPost(){
        return ResponseUtils.constructOKResponse("successpost", "duiya");
    }




}
