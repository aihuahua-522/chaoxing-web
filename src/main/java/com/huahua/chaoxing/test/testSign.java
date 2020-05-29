package com.huahua.chaoxing.test;


import com.huahua.chaoxing.bean.CourseBean;
import com.huahua.chaoxing.bean.PicBean;
import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.CookiesUtil;
import com.huahua.chaoxing.util.EmailUtil;
import com.huahua.chaoxing.util.JsonUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.huahua.chaoxing.listener.Listener.USER_MAP;

public class testSign {
    private static final Pattern pattern = Pattern.compile("\\d{5,}");
    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(50,
            Integer.MAX_VALUE,
            10, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(),
            r -> new Thread(r, "爱花花"));

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        ArrayList<UserBean> all = userService.getAll();
        all.forEach((u) -> executorService.execute(new signRunning(u)));
        ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(1);
        service.schedule(new testRunning(), 30, TimeUnit.SECONDS);
    }

    static class testRunning implements Runnable {
        @Override
        public void run() {
            System.out.println("线程池中线程数目：" + executorService.getPoolSize() + "，队列中等待执行的任务数目：" + executorService.getQueue().size() + "，已执行完的任务数目：" + executorService.getCompletedTaskCount());
        }
    }


    static class signRunning implements Runnable {

        private final HashMap<String, String> userMap;
        private final UserBean userBean;
        int i = 0;

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
                ArrayList<PicBean> picBeans = userBean.getPicBeans();
                if (userBean.getName() == null || userBean.getName().isEmpty()) {
                    getUserName(cookie);
                }
                String name = URLEncoder.encode(userBean.getName(), "utf-8");
                beginExecution(email, cookie, userBean.getCourseBeans(), name, picBeans, signPlace);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        private void beginExecution(String email, HashMap<String, String> cookie, ArrayList<CourseBean> courseBeans, String name, ArrayList<PicBean> picBeans, String signPlace) throws Exception {
            for (CourseBean courseBean : courseBeans) {
                System.out.println((++i) + "\t" + courseBean.getCourseName());
                String signUrl = courseBean.getSignUrl();
                Connection.Response response = Jsoup.connect(signUrl).cookies(cookie).timeout(300000).execute();
                Document document = response.parse();
                if (!document.title().contains("学生端-活动首页")) {
                    CookiesUtil.refreshCookies(userBean);
                    System.out.println("标题" + document.title());
                }
                Elements elements = document.select("#startList > div> div");
                judgeType(email, cookie, name, picBeans, courseBean, elements, signPlace);
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
                    signTime = "嘤嘤嘤.手动看他不香吗";
                }
                String signText = element.selectFirst(".Mct_center a[shape]").text();
                String signT = element.selectFirst(" a[shape]").text();
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
                                "活动类型 -> " + "\t" + signT + "\n" +
                                "班级名称 -> " + "\t" + courseBean.getClassName() + "\n" +
                                "活动标题 -> " + "\t" + signText + "\n" +
                                "剩余时间 -> " + signTime;
                        userMap.put(activeId, activeId);
                        EmailUtil.sendMail(email, message);
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
                EmailUtil.sendMail(email, message);
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
                EmailUtil.sendMail(email, message);
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
                EmailUtil.sendMail(email, message);
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
                EmailUtil.sendMail(email, message);
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
                Document signBody = Jsoup.connect(finalSignUrl).timeout(300000).cookies(cookie).execute().parse();
                String signState = signBody.getElementsByTag("body").text();
                if ("您已签到过了".equals(signState)) {
                    userMap.put(activeId, activeId);
                } else {
                    String message = "========这是签到=========" + "\n" + "班级名称-> " + courseBean.getCourseName() + "\n" +
                            "课程名称 -> " + courseBean.getClassName() + "\n" +
                            "签到类型 -> " + signText + "\n" +
                            "签到状态 -> " + signState + "\n" +
                            "剩余时间 -> " + signTime + "\n" +
                            "=========签到结束========";
                    EmailUtil.sendMail(email, message);
                }
            } catch (Exception e) {
                EmailUtil.sendMail(email, courseBean.getCourseName() + "可能签到失败(服务器会自动重试),建议手动看看");
                e.printStackTrace();
                userMap.remove(activeId);
            }

        }


        private void getUserName(HashMap<String, String> cookies) throws Exception {
            String getName = "http://i.chaoxing.com/base";
            Document document = Jsoup.connect(getName).cookies(cookies).timeout(300000).get();
            System.out.println(document);
            String name = document.select(".user-name").text();
            System.out.println("name" + name);
            UserServiceImpl service = new UserServiceImpl();
            userBean.setName(name);
            service.updateUser(userBean);
        }

    }
}
