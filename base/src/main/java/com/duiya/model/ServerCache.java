package com.duiya.model;

import java.io.Serializable;
import java.security.Key;

public class ServerCache implements Serializable {
    private static final long serialVersionUID = 7709922890298917842L;

    private String IP;
    private String IPHASH6;
    private String BASEURL;
    private Key PUBLICKEY;

    public Key getPUBLICKEY() {
        return PUBLICKEY;
    }

    public void setPUBLICKEY(Key PUBLICKEY) {
        this.PUBLICKEY = PUBLICKEY;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getIPHASH6() {
        return IPHASH6;
    }

    public void setIPHASH6(String IPHASH6) {
        this.IPHASH6 = IPHASH6;
    }

    public String getBASEURL() {
        return BASEURL;
    }

    public void setBASEURL(String BASEURL) {
        this.BASEURL = BASEURL;
    }
}
