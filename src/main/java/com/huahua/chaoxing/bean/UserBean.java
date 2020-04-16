package com.huahua.chaoxing.bean;

import java.math.BigInteger;
import java.util.ArrayList;

/*
 *@author Administrator
 */

public class UserBean {
    public UserBean() {
    }

    private BigInteger tel;
    private String pass;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UserBean(BigInteger tel, String pass) {
        this.tel = tel;
        this.pass = pass;
    }

    private String signPlace;
    private String email;
    private String cookies;
    private ArrayList<PicBean> picBeans;
    private ArrayList<CourseBean> courseBeans;

    public ArrayList<CourseBean> getCourseBeans() {
        return courseBeans;
    }

    public void setCourseBeans(ArrayList<CourseBean> courseBeans) {
        this.courseBeans = courseBeans;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getTel() {
        return tel;
    }

    public void setTel(BigInteger tel) {
        this.tel = tel;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSignPlace() {
        return signPlace;
    }

    public void setSignPlace(String signPlace) {
        this.signPlace = signPlace;
    }

    public ArrayList<PicBean> getPicBeans() {
        return picBeans;
    }

    public void setPicBeans(ArrayList<PicBean> picBeans) {
        this.picBeans = picBeans;
    }
}