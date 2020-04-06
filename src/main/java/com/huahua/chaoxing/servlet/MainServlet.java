package com.huahua.chaoxing.servlet;

import com.huahua.chaoxing.bean.CourseBean;
import com.huahua.chaoxing.bean.PicBean;
import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.DateUtil;
import com.huahua.chaoxing.util.EmailUtil;
import com.huahua.chaoxing.util.JsonUtil;
import org.jsoup.Connection;
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
import java.math.BigInteger;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.huahua.chaoxing.listener.Listener.USER_MAP;

@WebServlet(name = "MainServlet", value = "/SignMain")
public class MainServlet extends HttpServlet {
    private static Pattern pattern = Pattern.compile("\\d{5,}");
    private static ExecutorService executorService = new ThreadPoolExecutor(100,
            Integer.MAX_VALUE,
            10, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(10), r -> new Thread(r, "爱花花"));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int s = Integer.parseInt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern("HH")));
        if (s > 20 || s < 7) {
            response.getWriter().print("未到扫描时间");
            return;
        }
        UserService userService = new UserServiceImpl();
        ArrayList<UserBean> all = userService.getAll();
        response.getWriter().println("账号总数" + all.size() + "\n" + DateUtil.getTime());
        all.forEach((u) -> executorService.execute(new signRunning(u)));
    }


    static class signRunning implements Runnable {

        private final HashMap<String, String> userMap;
        int i = 0;
        private UserBean userBean;

        public signRunning(UserBean userbean) {
            this.userBean = userbean;
            userMap = USER_MAP.get(String.valueOf(userBean.getTel())) != null ? USER_MAP.get(String.valueOf(userBean.getTel())) : new HashMap<>();
            USER_MAP.put(String.valueOf(userBean.getTel()), userMap);
        }

        @Override
        public void run() {
            try {
                HashMap<String, String> cookie = JsonUtil.getMapByString(userBean.getCookies());
                String signPlace = URLEncoder.encode(userBean.getSignPlace(), "utf-8");
                String email = userBean.getEmail();
                BigInteger tel = userBean.getTel();
                ArrayList<PicBean> picBeans = userBean.getPicBeans();
                String name = URLEncoder.encode(getUserName(cookie), "utf-8");
                beginExecution(email, cookie, userBean.getCourseBeans(), name, picBeans, signPlace);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void beginExecution(String email, HashMap<String, String> cookie, ArrayList<CourseBean> courseBeans, String name, ArrayList<PicBean> picBeans, String signPlace) throws Exception {
            for (CourseBean courseBean : courseBeans) {
                System.out.println((++i) + "\t" + courseBean.getCourseName());
                String signUrl = courseBean.getSignUrl();
                Document document = Jsoup.connect(signUrl).cookies(cookie).timeout(30000).execute().parse();
                Elements elements = document.select("#startList > div> div");
                judgeType(email, cookie, name, picBeans, courseBean, elements, signPlace);
                getWorker(email, cookie, courseBean);
            }

        }

        private void getWorker(String email, HashMap<String, String> cookie, CourseBean courseBean) throws Exception {
            Document workAllDocument = Jsoup.connect(courseBean.getWorkUrl()).cookies(cookie).timeout(30000).method(Connection.Method.GET).execute().parse();
            Elements elements = workAllDocument.select(".titTxt [style]");
            for (Element element : elements) {
                String title = element.select(".clearfix > a").attr("title");
                String startTime = element.select(" span:nth-child(2) > span").text();
                String endTime = element.select(" span:nth-child(3) > span").text();
                String workState = element.select(" span:nth-child(4) > strong").text();
                String activeId = element.select(".Btn_red_1 .fr .inspectTask").attr("data");

                // TODO 发送邮件 有作业

                String message = "班级名称-> " + courseBean.getCourseName() + "\n" +
                        "课程名称 -> " + "\t" + courseBean.getClassName() + "\n" +
                        "作业名称 -> " + "\t" + title + "\n" +
                        "开始时间 -> " + "\t" + startTime + "\n" +
                        "结束时间 -> " + "\t" + endTime + "\n" +
                        "提交状态 -> " + "\t" + workState;
                try {
                    executorService.execute(new EmailUtil.emailSendRunning(email, message));
                    userMap.put(String.valueOf(userBean.getTel()), activeId);
                } catch (Exception e) {
                    userMap.remove(activeId);
                    e.printStackTrace();
                }

            }

        }

        private void judgeType(String email, HashMap<String, String> cookie, String name, ArrayList<PicBean> picBeans, CourseBean courseBean, Elements elements, String signPlace) throws Exception {
            for (Element element : elements) {
                String state = element.select("div > dl > a > dd ").text();
                String s = element.attr("onclick");
                Matcher matcher = pattern.matcher(s);
                String activeId = matcher.find() ? matcher.group() : "";
                String signTime = element.selectFirst(".Color_Orang").text();
                if (signTime == null || signTime.isEmpty()) {
                    signTime = "超星问题,显示不出剩余时间";
                }
                String signText = element.selectFirst(".Mct_center a[shape]").text();
                if (userMap.get(activeId) != null) {
                    continue;
                }
                switch (state) {
                    case "签到":
                        signByActiveId(email, signTime, signText, courseBean, cookie, name, picBeans, signPlace, activeId);
                        break;
                    case "问卷":
                        answerByActiveId(email, signTime, signText, courseBean, activeId);
                        break;
                    case "抢答":
                        answerQuickly(email, signTime, signText, courseBean, activeId);
                        break;
                    case "测验":
                        testVerification(email, courseBean, signText, signTime, activeId);
                        break;
                    case "评分":
                        score(email, courseBean, signText, signTime, activeId);
                        break;
                    default:
                        String message = "课程名称 -> " + "\t" + courseBean.getCourseName() + "\n" +
                                "班级名称 -> " + "\t" + courseBean.getClassName() + "\n" +
                                "活动标题 -> " + "\t" + signText + "\n" +
                                "剩余时间 -> " + signTime;
                        userMap.put(activeId, activeId);
                        executorService.execute(new EmailUtil.emailSendRunning(email, message));
                        break;
                }
            }
        }

        private void score(String email, CourseBean courseBean, String signText, String signTime, String activeId) {

            // TODO 发送邮件 有评分
            String message = "课程名称 -> " + "\t" + courseBean.getCourseName() + "\n" +
                    "班级名称 -> " + "\t" + courseBean.getClassName() + "\n" +
                    "评分标题 -> " + "\t" + signText + "\n" +
                    "剩余时间 -> " + signTime;
            try {
                executorService.execute(new EmailUtil.emailSendRunning(email, message));
                userMap.put(activeId, activeId);
            } catch (Exception e) {
                userMap.remove(activeId);
                e.printStackTrace();
            }

        }

        private void testVerification(String email, CourseBean courseBean, String signText, String signTime, String activeId) {

            // TODO  发送邮件 有测验
            String message = "课程名称 -> " + "\t" + courseBean.getCourseName() + "\n" +
                    "班级名称 -> " + "\t" + courseBean.getClassName() + "\n" +
                    "测验标题 -> " + "\t" + signText + "\n" +
                    "剩余时间 -> " + "\t" + signTime;
            try {
                executorService.execute(new EmailUtil.emailSendRunning(email, message));
                userMap.put(activeId, activeId);
            } catch (Exception e) {
                e.printStackTrace();
                userMap.remove(activeId);
            }
        }

        private void answerQuickly(String email, String signTime, String signText, CourseBean courseBean, String activeId) throws Exception {
            // TODO 发送邮件 有抢答
            String message = "课程名称-> " + courseBean.getCourseName() + "\n" +
                    "班级名称 -> " + courseBean.getClassName() + "\n" +
                    "抢答标题 -> " + signText + "\n" +
                    "剩余时间 -> " + signTime;
            try {
                executorService.execute(new EmailUtil.emailSendRunning(email, message));
                userMap.put(activeId, activeId);
            } catch (Exception e) {
                e.printStackTrace();
                userMap.remove(activeId);
            }
        }

        private void answerByActiveId(String email, String signTime, String signText, CourseBean courseBean, String activeId) throws Exception {
            // TODO 发送邮件 有问卷
            String message = "课程名称-> " + courseBean.getCourseName() + "\n" +
                    "班级名称 -> " + courseBean.getClassName() + "\n" +
                    "问卷标题 -> " + signText + "\n" +
                    "剩余时间 -> " + signTime;
            try {
                executorService.execute(new EmailUtil.emailSendRunning(email, message));
                userMap.put(activeId, activeId);
            } catch (Exception e) {
                e.printStackTrace();
                userMap.remove(activeId);
            }
        }

        private void signByActiveId(String email, String signTime, String signText, CourseBean courseBean, HashMap<String, String> cookie, String name, ArrayList<PicBean> picBeans, String signPlace, String activeId) throws Exception {
            // TODO 发送邮件  开始签到
            String finalSignUrl = "https://mobilelearn.chaoxing.com/pptSign/stuSignajax?name="
                    + name
                    + "&address="
                    + signPlace
                    + "&activeId="
                    + activeId
                    + "&uid="
                    + cookie.get("_uid")
                    + "&clientip=&latitude=-1&longitude=-1&fid="
                    + cookie.get("fid")
                    + "&appType=15&ifTiJiao=1"
                    + "&objectId=" + picBeans.get(new Random().nextInt(picBeans.size())).getObjectid();
            try {
                Document signBody = Jsoup.connect(finalSignUrl).timeout(30000).cookies(cookie).execute().parse();
                String signState = signBody.getElementsByTag("body").text();
                if ("您已签到过了".equals(signState)) {
                    userMap.put(activeId, activeId);
                }
                String message = "========这是签到=========" + "\n" + "班级名称-> " + courseBean.getCourseName() + "\n" +
                        "课程名称 -> " + courseBean.getClassName() + "\n" +
                        "签到类型 -> " + signText + "\n" +
                        "签到状态 -> " + signState + "\n" +
                        "剩余时间 -> " + signTime + "\n" +
                        "=========签到结束========";
                executorService.execute(new EmailUtil.emailSendRunning(email, message));
            } catch (Exception e) {
                executorService.execute(new EmailUtil.emailSendRunning(email, courseBean.getCourseName() + "可能签到失败(服务器会自动重试),建议手动看看"));
                e.printStackTrace();
                userMap.remove(activeId);
            }

        }


        private String getUserName(HashMap<String, String> cookies) throws IOException {
            String getName = "http://i.chaoxing.com/base";
            return Jsoup.connect(getName).cookies(cookies).timeout(30000).get().select(".user-name").text();
        }

/*        private ArrayList<CourseBean> getClassBeans(String email, BigInteger tel, HashMap<String, String> cookies) throws Exception {
            // 4. 登录成功-->将课程封装list
            ArrayList<CourseBean> courseBeans = new ArrayList<>();
            String getClassUrl = "http://mooc1-2.chaoxing.com/visit/courses";
            Document classDocument = Jsoup.connect(getClassUrl).cookies(cookies).method(Connection.Method.GET)
                    .timeout(30000).get();
            if (classDocument.title().contains("用户登录")) {
                // TODO 这里可以发送邮件 重新登录
                executorService.submit(new EmailUtil.emailSendRunning(email, "cookie可能失效,推荐重新提交"));
            }

            Elements allClassElement = classDocument.select(".ulDiv > ul > li[style]");
            for (Element classElement : allClassElement) {
                String courseId = classElement.select("[name = courseId]").attr("value");
                String href = classElement.select(" .Mcon1img > a").attr("href");
                String enc = href.split("enc=")[1];
                String cpi = href.split("cpi=")[1];
                String classId = classElement.select("[name = classId]").attr("value");
                //课程名
                String courseName = classElement.select(".clearfix > a ").attr("title");
                //班级名
                String className = classElement.select(".Mconright > p ").get(2).attr("title");
                //教师
                String teacher = classElement.select(".Mconright > p ").get(0).attr("title");
                //任务地址
                String signUrl = "https://mobilelearn.chaoxing.com/widget/pcpick/stu/index?courseId=" + courseId + "&jclassId=" + classId;

                String workUrl = "https://mooc1-1.chaoxing.com/work/getAllWork?classId=" + classId + "&courseId=" + courseId + "&isdisplaytable=2&mooc=1&ut=s&enc=" + enc + "&cpi=" + cpi;

                courseBeans.add(new CourseBean(,signUrl, courseId, className, courseName, classId, teacher, workUrl));

            }
            System.out.println(JsonUtil.objectToJson(courseBeans));
            return courseBeans;
        }*/
    }
}
