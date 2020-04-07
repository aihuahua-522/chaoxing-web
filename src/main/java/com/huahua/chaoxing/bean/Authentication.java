package com.huahua.chaoxing.bean;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Authentication extends Authenticator {
    private String username = null;
    private String password = null;

    public Authentication() {

    }

    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
