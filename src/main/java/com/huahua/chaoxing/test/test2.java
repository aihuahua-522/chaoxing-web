package com.huahua.chaoxing.test;

import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.JsonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class test2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        UserServiceImpl service = new UserServiceImpl();
        ArrayList<UserBean> list = service.getAll();
        System.out.println(list.size());
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            num++;
            System.out.println(list.get(i).getName());
            if (list.get(i).getName() == null || list.get(i).getName().equals("")) {
                Future<?> submit = executor.submit(new runnable(list.get(i)));
                submit.get();
            }
        }
        System.out.println(num);

    }


    static class runnable implements Runnable {

        private final UserBean userBean;

        public runnable(UserBean userBean) {
            this.userBean = userBean;
        }

        @Override
        public void run() {
            getUserName(userBean);
        }

        private void getUserName(UserBean userBean) {
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


    }
}
