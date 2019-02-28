package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.init.BaseConfig;
import com.duiya.model.Location;
import com.duiya.service.FileService;
import com.duiya.utils.ResponseEnum;
import com.duiya.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("backup")
public class BackupController {
    private Logger logger = LoggerFactory.getLogger(BackupController.class);

    @Autowired
    private FileService fileService;

    /**
     * 其他服务器请求这个服务器文件
     * @param location
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin//跨域
    public void fileGet(@RequestParam("location") String location, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            logger.error("backup/get-->获取输出流失败:", e);
            return;
        }
        Location location1 = Location.getLocation(location);
        try {
            /*图片地址不符合规范*/
            if (location1 == null) {
                try {
                    out.print(ResponseEnum.ERROR_LOCATION);
                    out.flush();
                } catch (IOException e) {
                    logger.error("backup/get-->输出失败:", e);
                }
                return;
            }

            String path = location1.getPath(BaseConfig.ROOT_LOCATION);
            File file = new File(path);
            byte[] img = null;
            if (!file.exists()) {
                /*没有图片*/
                try {
                    out.print(ResponseEnum.PIC_NOTFOUND);
                    out.flush();
                } catch (IOException e) {
                    logger.error("backup/get-->输出失败:", e);
                }
                return;
            }else {
                try {
                    out.print(ResponseEnum.PIC_BACKUPOK);
                    out.print(location);
                    fileService.getAndWriteFileL(out, path);
                    out.flush();
                } catch (Exception e) {
                    logger.error("backup/get-->获取图片失败:", e);
                }
                return;
            }
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("backup/get-->关闭输出流失败:", e);
                }
            }
        }

    }

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
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getRemoteAddr();
        if (fileService.hasIp(ip)) {
            // 判断文件是否为空
            if (multipartFiles != null && multipartFiles.length >= 1) {
                try {
                    fileService.saveFile(multipartFiles);
                } catch (Exception e) {
                    logger.error("failed to upload file", e);
                    return ResponseUtil.constructUnknownErrorResponse("unknown error, please try again later");
                }
            } else {
                return ResponseUtil.constructArgErrorResponse("the file is empty");
            }
        }
        return ResponseUtil.constructArgErrorResponse("unknown hosts");
    }

}
