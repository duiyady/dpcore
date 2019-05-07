package com.duiya.controller;

import com.alibaba.fastjson.JSONObject;
import com.duiya.init.BaseConfig;
import com.duiya.model.Location;
import com.duiya.service.BackupService;
import com.duiya.service.FileService;
import com.duiya.service.KeyService;
import com.duiya.utils.ResponseUtil;
import com.duiya.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 这个接口是用来用户访问的，用户上传图片和获得图片
 */

@Controller
@RequestMapping("file")
@CrossOrigin
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private KeyService keyService;

    @Autowired
    private BackupService backupService;

    /**
     * 上传文件
     * @param multipartFiles
     * @param account
     * @param flag
     * @return
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject fileUpLoad(@RequestParam("file") MultipartFile[] multipartFiles,
                                 @RequestParam(value = "account") String account,
                                 @RequestParam(value = "flag", required = false) String flag) {


        logger.info("invokeinvoke--------------------file/upload?account:" + account + ",flag:" + flag);

        /*是否需要验证*/
        if (BaseConfig.VERIFIED) {
            if (StringUtil.isBlank(flag)) {
                return ResponseUtil.constructArgErrorResponse("you should get a key");
            }
            if (keyService.verify(account, flag) == false) {
                return ResponseUtil.constructArgErrorResponse("flag error");
            }
        }

        // 判断文件是否为空
        if (multipartFiles != null && multipartFiles.length >= 1) {
            try {
                //文件数量过多
                if (multipartFiles.length > 9) {
                    return ResponseUtil.constructOversizeResponse("please upload less than 9 files");
                }

                //进行文件保存
                Map<Integer, String> result = fileService.saveFile(account, multipartFiles);
                if (result != null) {
                    return ResponseUtil.constructOKResponse("requst succeed", result);
                } else {
                    return ResponseUtil.constructUnknownErrorResponse("unknow error, please try again later");
                }

            } catch (Exception e) {
                logger.error("failed to upload file", e);
                return ResponseUtil.constructUnknownErrorResponse("unknown error, please try again later");
            }
        } else {
            return ResponseUtil.constructArgErrorResponse("the file is empty");
        }
    }

    /**
     * 获取文件
     * @param location
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void fileGet(@RequestParam("location") String location, HttpServletResponse httpServletResponse) {

        logger.info("invoke--------------------file/get?location:" + location);

        Location location1 = Location.getLocation(location);
        //图片地址不符合规范
        if (location1 == null) {
            return;
        }
        String path = location1.getPath(BaseConfig.ROOT_LOCATION);
        File file = new File(path);
        httpServletResponse.setContentType("image/png");
        ServletOutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
            if (!file.exists()) {
                //文件不存在而且第一次保存主机是本机，则文件肯定不对
                if (location1.getIPHash6().equals(BaseConfig.IPHASH6)) {
                    return;
                } else {
                    //向文件第一个主机请求同步
                    backupService.getAndWriteFile(location1, path, os);
                }
            } else {
                fileService.getAndWriteFile(path, os);
            }
        } catch (Exception e) {
            logger.error("返回数据失败", e);
            return;
        }
        try {
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;

    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject fileDelete(@RequestParam(name = "account") String account,
                                 @RequestParam(name = "key") String key,
                                 @RequestParam(name = "location") String location) {
        if (!StringUtil.isBlank(account, key, location)) {
            //进行删除，要检验那个图片是否是那个用户的

        } else {
            return ResponseUtil.constructArgErrorResponse("the account, key and location cannot be empty");
        }
        return null;
    }


    /**
     * 返回的flag是用用户的公钥加密的，用户用自己的私钥解密后随着图片一起传回来
     * @param account
     * @param pass
     * @return
     */
    @RequestMapping(value = "getFlag", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getFlag(@RequestParam("account")String account, @RequestParam("pass")String pass){
        logger.info("invoke--------------------file/getFlag?account:" + account);
        try {
            String flag = keyService.getUploadFlag(account, pass);
            if(flag == null){
                return ResponseUtil.constructArgErrorResponse("c参数错误");
            }else{
                return ResponseUtil.constructOKResponse("success", flag);
            }
        } catch (Exception e) {
            logger.error("获取上传图片的flag失败", e);
            return ResponseUtil.constructUnknownErrorResponse("unknown error");
        }
    }

}
