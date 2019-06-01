package com.duiya.service;

import com.duiya.model.ResponseModel;
import com.duiya.model.Slave;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MonitorService {
    private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

    public static ResponseModel isAlive(Slave slave) {
        String url = slave.getBaseUrl() + "/monitor/alive";
        ResponseModel responseModel = null;
        try {
            responseModel = HttpUtil.sendGetModel(url, null);
        } catch (IOException e) {
            logger.error("心跳检测时http失败", e);
        }
        return responseModel;
    }

    public static ResponseModel upAlive(Slave slave) {
        String url = slave.getBaseUrl() + "/monitor/updateState";
        ResponseModel responseModel = null;
        try {
            responseModel = HttpUtil.sendGetModel(url, null);
        } catch (IOException e) {
            logger.error("心跳检测时http失败", e);
        }
        return responseModel;
    }



    public static ResponseModel requestASYN(Slave slave, String baseparam, String s1) {
        String url = slave.getBaseUrl() + "/monitor/sync";
        String s2 = RSAUtil.encrypt(s1, slave.getPublicKey());
        String param = null;
        ResponseModel responseModel = null;
        try {
            param = baseparam + "&flag=" + URLEncoder.encode(s2,"utf8");
        } catch (UnsupportedEncodingException e) {
            logger.error("发送同步信号时转码失败", e);
            return responseModel;
        }
        try {
            responseModel = HttpUtil.sendPostModel(url, param);
        } catch (IOException e) {
            logger.error("发送同步信号时http失败", e);
        }
        return responseModel;
    }
}
