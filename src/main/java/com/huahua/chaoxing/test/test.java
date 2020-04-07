package com.huahua.chaoxing.test;

import com.huahua.chaoxing.bean.CourseBean;
import com.huahua.chaoxing.bean.PicBean;
import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.i.CourseService;
import com.huahua.chaoxing.service.i.PicService;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.CourseServiceImpl;
import com.huahua.chaoxing.service.impl.PicServiceImpl;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.JsonUtil;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class test {
    BigInteger tel = new BigInteger("17702020202231231");
    String pass = "huahua";

    public static void main(String[] args) {
        String tel = "hisdhowrhiow.,.,.权威q1231231.12,312.3,123jqocwcoj  ";
        tel = tel.replaceAll("\\D", "");
        System.out.println(tel);

    }

    @Test
    public void testUserMapper() {

        // 查询所有
        UserService service = new UserServiceImpl();
        ArrayList<UserBean> all = service.getAll();
        System.out.println(all);


        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("name", "aihuahua");
        // 添加用户 and cookies
        BigInteger tel = new BigInteger("17702020202231231");
        ResultBean<String> login = service.login(tel, "huahua", "晚安", cookies, "19823238492");
        System.out.println("添加用户" + JsonUtil.objectToJson(login));

        // 查询用户
        UserBean oneUser = service.getOneUser(tel, "aihuahua");
        System.out.println("查询用户" + JsonUtil.objectToJson(oneUser));


        ResultBean<String> resultBean = service.deleteUser(tel, pass);
        System.out.println("删除用户" + JsonUtil.objectToJson(resultBean));

    }

    @Test
    public void testCourseMapper() {

        CourseService service = new CourseServiceImpl();

        // 测试添加课程
        CourseBean courseBean = new CourseBean(tel, "signUrk", "courseId", "班级名", "课程名", "classId", "教师", "workUrl");
        ArrayList<CourseBean> list = new ArrayList<>();
        list.add(courseBean);
        ResultBean<String> resultBean = service.addCourse(JsonUtil.objectToJson(list), String.valueOf(tel), "huahua");
        System.out.println(JsonUtil.objectToJson(resultBean));

        // 测试删除课程
        ResultBean<String> resultBean1 = service.deleteAllCourse(String.valueOf(tel), "111");
        System.out.println(JsonUtil.objectToJson(resultBean1));
    }

    @Test
    public void testPicMapper() {

        PicService service = new PicServiceImpl();

        PicBean picBean = new PicBean(0, "1234", tel);
        PicBean picBean2 = new PicBean(0, "234", tel);
        ArrayList<PicBean> picBeanArrayList = new ArrayList<>();
        picBeanArrayList.add(picBean);
        picBeanArrayList.add(picBean2);
        // 添加图片
        ResultBean<String> setPic = service.setPic(tel, "pass", JsonUtil.objectToJson(picBeanArrayList));
        System.out.println(JsonUtil.objectToJson(setPic));


        // 获取图片
        ResultBean<String> getPic = service.getPic(tel);
        System.out.println(getPic);

        //删除单张图片
        ResultBean<String> deletePic = service.deletePic(tel, String.valueOf(123));
        System.out.println(deletePic);
        // 删除所有图片
        ResultBean<String> deleteAllPic = service.deleteAllPic(tel, "pass");
        System.out.println(JsonUtil.objectToJson(deleteAllPic));

    }

    @Test
    public void test() throws IOException {

        String message = "课程名称 -> " + "\t" + " courseBean.getCourseName()" + "\n" +
                "班级名称 -> " + "\t" + " courseBean.getClassName()" + "\n" +
                "活动标题 -> " + "\t" + "signText" + "\n" +
                "剩余时间 -> " + "signTime";


//        1586262003730

        Instant now = Instant.ofEpochMilli(Long.parseLong("1586262003730"));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        System.out.println(localDateTime);
    }


}