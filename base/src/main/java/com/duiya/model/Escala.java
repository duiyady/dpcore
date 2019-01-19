package com.duiya.model;

import java.io.Serializable;

/**
 * 按比例选择时用这个
 */
public class Escala implements Serializable {


    private static final long serialVersionUID = -1927326120578298760L;
    private int flag;

    private String IP;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
