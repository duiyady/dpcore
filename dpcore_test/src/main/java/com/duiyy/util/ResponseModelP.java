package com.duiyy.util;

import java.util.Map;

public class ResponseModelP {
    private static final long serialVersionUID = 1297296862435171402L;
    private int code;
    private String msg;
    private Map<Integer, String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<Integer, String> getData() {
        return data;
    }

    public void setData(Map<Integer, String> data) {
        this.data = data;
    }
}
