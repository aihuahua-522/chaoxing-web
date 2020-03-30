package com.huahua.chaoxing.service.i;

import com.huahua.chaoxing.bean.ResultBean;

public interface CourseService {

    /**
     * 添加课程
     *
     * @param courseBeans 课程信息集合的字符串
     * @return
     */
    ResultBean<String> addCourse(String courseBeans, String tel,String pass);


    /**
     * 删除所有课程
     *
     * @param tel
     * @param pass
     * @return
     */
    ResultBean<String> deleteAllCourse(String tel, String pass);


}
