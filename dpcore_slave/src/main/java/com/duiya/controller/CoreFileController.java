package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.init.DPCoreInit;
import com.duiya.model.Location;
import com.duiya.service.FileManageService;
import com.duiya.utils.ResponseEnum;
import com.duiya.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 这个接口是暴露给数据同步的，只能集群中的服务器使用，调用这个接口时需要拦截先验证ip是否符合
 * 这里同步文件
 * 用于集群同步文件的接口
 */

@Controller
@RequestMapping("corefile")
public class CoreFileController {

    private Logger logger = LoggerFactory.getLogger(CoreFileController.class);

    @Autowired
    private FileManageService fileManageService;


    /**
     * 其他服务器请求此服务器文件
     *
     * @param location
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin//跨域
    public JSONObject fileGet(@RequestParam("location") String location) {

        Location location1 = Location.getLocation(location);
        //图片地址不符合规范
        if (location1 == null) {
            return ResponseUtils.constructArgErrorResponse("the location is error");
        }

        File file = new File(location1.getPath(DPCoreInit.ROOT_LOCATION));
        byte[] img = null;
        if (!file.exists()) {
            return ResponseUtils.constructResponse(ResponseEnum.NULL_PIC, "can not find ther pic", null);
        }
        img = fileManageService.getFile(location1.getPath(DPCoreInit.ROOT_LOCATION));
        if (img == null) {
            return ResponseUtils.constructUnknownErrorResponse("unknown error");
        }
        return ResponseUtils.constructOKResponse("success", img);
    }

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
        if (fileManageService.hasIp(ip)) {
            // 判断文件是否为空
            if (multipartFiles != null && multipartFiles.length >= 1) {
                try {
                    fileManageService.saveFile(multipartFiles);
                } catch (Exception e) {
                    logger.error("failed to upload file", e);
                    return ResponseUtils.constructUnknownErrorResponse("unknown error, please try again later");
                }
            } else {
                return ResponseUtils.constructArgErrorResponse("the file is empty");
            }
        }
        return ResponseUtils.constructArgErrorResponse("unknown hosts");
    }


}
