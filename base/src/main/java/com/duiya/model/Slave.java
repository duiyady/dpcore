package com.duiya.model;

import java.io.Serializable;
import java.security.Key;

public class Slave implements Serializable {
    private static final long serialVersionUID = -5533065529826005736L;
    private String IPHash6;

    private String IP;

    private Key publicKey;

    private int state;//1可用 2一次检测不到 3两次检测不到 4三次检测不到(移除) 10设置的不可用

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIPHash6() {
        return IPHash6;
    }

    public void setIPHash6(String IPHash6) {
        this.IPHash6 = IPHash6;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Key getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Key publicKey) {
        this.publicKey = publicKey;
    }
}
