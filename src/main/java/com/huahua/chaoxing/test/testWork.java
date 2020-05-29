package com.huahua.chaoxing.test;

import com.huahua.chaoxing.bean.CourseBean;
import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.EmailUtil;
import com.huahua.chaoxing.util.JsonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigInteger;
import java.util.HashMap;

public class testWork {

    public static void main(String[] args) throws Exception {
        UserServiceImpl userService = new UserServiceImpl();
        UserBean oneUser = userService.getOneUser(BigInteger.valueOf(Long.parseLong("17707962579")), "1234567890");
        for (CourseBean courseBean : oneUser.getCourseBeans()) {
            getWorker(oneUser.getEmail(), JsonUtil.getMapByString(oneUser.getCookies()), courseBean);
        }

    }

    private static void getWorker(String email, HashMap<String, String> cookie, CourseBean courseBean) throws Exception {
        int index = 0;
        StringBuilder sb = new StringBuilder();
        Document document = Jsoup.connect("https://mobilelearn.chaoxing.com/task/getStuWorkAndExamSkipUrl?courseId=" + courseBean.getCourseId() + "&classId=" + courseBean.getClassId()).followRedirects(true).cookies(cookie).get();
        // TODO 发送邮件 有作业
        Elements elements = document.select(".nav > li");
        sb.append(courseBean.getCourseName()).append("  未交作业如下:").append("\n").append("\n");
        for (Element element : elements) {
            if (element.select("span").text().contains("未交") && !element.select("img").attr("src").equals("/images/work/phone/task-work-gray.png")) {
                index++;
                sb.append("作业名称 -> ").append("\t").append(element.select("p").text()).append("\n").append("作业状态 -> ").append(element.select("span").text()).append(":你怎么还不做").append("\n").append("\n");
            }
        }

        if (index == 0) {
            return;
        }
        EmailUtil.sendMail(email, sb.toString());
    }

}