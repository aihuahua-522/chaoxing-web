package com.huahua.chaoxing.test;

import com.huahua.chaoxing.bean.*;
import com.huahua.chaoxing.service.i.CourseService;
import com.huahua.chaoxing.service.i.PicService;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.CourseServiceImpl;
import com.huahua.chaoxing.service.impl.PicServiceImpl;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.CookiesUtil;
import com.huahua.chaoxing.util.DateUtil;
import com.huahua.chaoxing.util.HttpUtil;
import com.huahua.chaoxing.util.JsonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class test {
    BigInteger tel = new BigInteger("17707962579");
    String pass = "huahua";

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        UserServiceImpl service = new UserServiceImpl();
        ArrayList<UserBean> list = service.getAll();
        System.out.println(list.size());
        for (UserBean userBean : list) {
            if (userBean.getName() == null || userBean.getName().equals("")) {
                try {
                    getUserName(userBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void getUserName(UserBean userBean) {
        System.out.println(userBean.getTel() + "   " + "执行");
        String getName = "http://i.chaoxing.com/base";
        try {
            Document document = Jsoup.connect(getName).cookies(JsonUtil.getMapByString(userBean.getCookies())).timeout(300000).get();
            System.out.println(document);
            String name = document.select(".user-name").text();
            System.out.println("name" + name);
            UserServiceImpl service = new UserServiceImpl();
            userBean.setName(name);
            service.updateUser(userBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void test01() {
        // 查询所有
        UserService service = new UserServiceImpl();
        ArrayList<UserBean> all = service.getAll();
        System.out.println(all);
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
    public void testQuery() {
        UserService service = new UserServiceImpl();
        // 查询用户
        UserBean oneUser = service.getOneUser(tel, "1234567890");
        System.out.println("查询用户" + JsonUtil.objectToJson(oneUser));

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


        CookiesUtil.refreshCookies(new UserBean(BigInteger.valueOf(Long.parseLong("17707962579")), "1234567890"));
    }

    @Test
    public void test2() {
        qqBean qqBean = new qqBean();
        qqBean.setUser_id("1986754601");
        qqBean.setMessage(DateUtil.getTime() + "\n" + "测试");
        try {
            HttpUtil.trustEveryone();
            Document post = Jsoup.connect("http://39.96.95.173:5700/send_private_msg")
                    .requestBody(JsonUtil.objectToJson(qqBean))
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .post();
            System.out.println(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() throws Exception {

        // 1、直接提交队列：设置为SynchronousQueue队列，SynchronousQueue是一个特殊的BlockingQueue，
        // 它没有容量，没执行一个插入操作就会阻塞，需要再执行一个删除操作才会被唤醒，
        // 反之每一个删除操作也都要等待对应的插入操作。

        //2、有界的任务队列：有界的任务队列可以使用ArrayBlockingQueue实现

        //3、无界的任务队列：无界任务队列可以使用LinkedBlockingQueue实现，如下所示

        //4、优先任务队列：优先任务队列通过PriorityBlockingQueue实现
        ThreadPoolExecutor start = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 31; i++) {
            start.execute(new running());
        }
        Thread.sleep(100000);
    }


    static class running implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}