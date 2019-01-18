package com.duiya.model;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户公钥
     */
    private String userSskey;

    public User(String userAccount, String userPassword, String userEmail, String userSskey) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userSskey = userSskey;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userSskey='" + userSskey + '\'' +
                '}';
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserSskey() {
        return userSskey;
    }

    public void setUserSskey(String userSskey) {
        this.userSskey = userSskey;
    }
}
