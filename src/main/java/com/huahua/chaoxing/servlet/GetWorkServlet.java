package com.huahua.chaoxing.servlet;

import com.huahua.chaoxing.bean.CourseBean;
import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.DateUtil;
import com.huahua.chaoxing.util.EmailUtil;
import com.huahua.chaoxing.util.JsonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "GetWorkServlet", value = "/GetWorkServlet")
public class GetWorkServlet extends HttpServlet {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(30, 30, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    private static void getWorker(String email, HashMap<String, String> cookie, ArrayList<CourseBean> courseBeans) throws Exception {
        for (int i = 0; i < courseBeans.size(); i++) {
            try {
                CourseBean courseBean = courseBeans.get(i);
                int index = 0;
                StringBuilder sb = new StringBuilder();
                Document document = Jsoup.connect("https://mobilelearn.chaoxing.com/task/getStuWorkAndExamSkipUrl?courseId=" + courseBean.getCourseId() + "&classId=" + courseBean.getClassId()).followRedirects(true).cookies(cookie).get();
                // TODO 发送邮件 有作业
                Elements elements = document.select(".nav > li");
                sb.append(courseBean.getCourseName()).append("  未交作业如下:").append("\n").append("\n");
                System.out.println(i + "     " + courseBeans.get(i).getCourseName());
                for (Element element : elements) {
                    if (element.select("span").text().contains("未交") && !element.select("img").attr("src").equals("/images/work/phone/task-work-gray.png")) {
                        index++;
                        sb.append("作业名称 -> ").append("\t").append(element.select("p").text()).append("\n").append("作业状态 -> ").append(element.select("span").text()).append(":你怎么还不做").append("\n").append("\n");
                    }
                }
                if (index == 0) {
                    continue;
                }
                EmailUtil.sendMail(email, sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService service = new UserServiceImpl();
        ArrayList<UserBean> userBeans = service.getAll();
        response.getWriter().println(DateUtil.getTime() + "\n" + "账号总数:" + userBeans.size());
        for (UserBean userBean : userBeans) {
            executor.execute(new runnable(userBean));
        }
    }

    static class runnable implements Runnable {

        UserBean userBean;

        public runnable(UserBean userBean) {
            this.userBean = userBean;
        }

        @Override
        public void run() {
            try {
                getWorker(userBean.getEmail(), JsonUtil.getMapByString(userBean.getCookies()), userBean.getCourseBeans());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
