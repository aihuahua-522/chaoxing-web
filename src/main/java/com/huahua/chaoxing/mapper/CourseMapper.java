package com.huahua.chaoxing.mapper;

import com.huahua.chaoxing.bean.CourseBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.ArrayList;

public interface CourseMapper {


    /**
     * 插入课程
     */
    void insertCourse(@Param("course") CourseBean course, @Param("tel") BigInteger tel);

    /**
     * 删除所有课程
     *
     * @param tel
     */
    void deleteAllCourse(@Param("tel") BigInteger tel);


    /**
     * 查询所有课程
     * @param tel
     */
    ArrayList<CourseBean> getAllCourse(@Param("tel") BigInteger tel);


    int deleteOneCoure(@Param("tel") BigInteger tel, @Param("courseId") String courseId, @Param("classId") String classId);
}
