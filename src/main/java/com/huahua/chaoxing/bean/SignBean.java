package com.huahua.chaoxing.bean;

public class SignBean {
    /**
     * 课程名
     */
    private String className;

    /**
     * 班级名
     */
    private String courseName;

    /**
     * 剩余时间
     */
    private String endTime;

    public SignBean(String className, String courseName, String endTime, String signState) {
        this.className = className;
        this.courseName = courseName;
        this.endTime = endTime;
        this.signState = signState;
    }

    /**
     * 签到状态
     */
    private String signState;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSignState() {
        return signState;
    }

    public void setSignState(String signState) {
        this.signState = signState;
    }
}
