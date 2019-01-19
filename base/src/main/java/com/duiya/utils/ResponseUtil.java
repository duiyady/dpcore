package com.duiya.utils;

import com.alibaba.fastjson.JSONObject;

public class ResponseUtil {
    /**
     * @author jipeng
     * @return
     *
     * 		构造返回json
     *
     */
    public static JSONObject constructResponse(int code, String msg, Object data) {
        JSONObject jo = new JSONObject();
        jo.put("code", code);
        jo.put("msg", msg);
        jo.put("data", data);
        return jo;
    }

    /**
     * 参数错误返回json
     *
     * @param msg
     * @return
     */
    public static JSONObject constructArgErrorResponse(String msg) {
        JSONObject jo = new JSONObject();
        jo.put("code", ResponseEnum.ARG_ERROR);
        jo.put("msg", msg);
        jo.put("data", null);
        return jo;
    }

    /**
     * 数据库错误返回json
     *
     * @param msg
     * @return
     */
    public static JSONObject constructDbErrorResponse(String msg) {
        JSONObject jo = new JSONObject();
        jo.put("code", ResponseEnum.DB_ERROR);
        jo.put("msg", msg);
        jo.put("data", null);
        return jo;
    }

    /**
     * 未知错误 返回json
     *
     * @param msg
     * @return
     */
    public static JSONObject constructUnknownErrorResponse(String msg) {
        JSONObject jo = new JSONObject();
        jo.put("code", ResponseEnum.UNKNOEN_ERROR);
        jo.put("msg", msg);
        jo.put("data", null);
        return jo;
    }

    /**
     * ok 返回
     * @param msg
     * @param data
     * @return
     */
    public static JSONObject constructOKResponse(String msg, Object data) {
        JSONObject jo = new JSONObject();
        jo.put("code", ResponseEnum.OK);
        jo.put("msg", msg);
        jo.put("data", data);
        return jo;
    }

    /**
     * 越权错误
     * @return
     */
    public static JSONObject constructNoUserResponse() {
        JSONObject jo = new JSONObject();
        jo.put("code", ResponseEnum.LIMIT_EROR);
        jo.put("msg", null);
        jo.put("data", null);
        return jo;
    }

    /**
     * 超过负载错误
     * @return
     */
    public static JSONObject constructOversizeResponse(String msg) {
        JSONObject jo = new JSONObject();
        jo.put("code", ResponseEnum.OVERSIZRE_ERROR);
        jo.put("msg", msg);
        jo.put("data", null);
        return jo;
    }

    public static JSONObject constructPartErrorResponse(Object data){
        JSONObject jo = new JSONObject();
        jo.put("code", ResponseEnum.PART_EROR);
        jo.put("msg", "请求重传");
        jo.put("data", data);
        return jo;
    }
}
