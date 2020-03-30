package com.huahua.chaoxing.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.Bidi;

/**
 * pic
 *
 * @author
 */
public class PicBean {

    private Integer pid;
    private BigInteger tel;
    private String objectid;

    public PicBean() {
    }

    public PicBean(Integer pid, String objectid, BigInteger tel) {
        this.pid = pid;
        this.objectid = objectid;
        this.tel = tel;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getObjectid() {
        return objectid;
    }

    public BigInteger getTel() {
        return tel;
    }

    public void setTel(BigInteger tel) {
        this.tel = tel;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

}