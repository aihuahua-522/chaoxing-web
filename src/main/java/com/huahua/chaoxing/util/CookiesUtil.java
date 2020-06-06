package com.huahua.chaoxing.util;

import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import sun.util.resources.cldr.rw.CurrencyNames_rw;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** @author LoveHuahua */
public class CookiesUtil {
  static ThreadPoolExecutor threadPoolExecutor =
      new ThreadPoolExecutor(
          50, Integer.MAX_VALUE, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

  public static void refreshCookies(UserBean userBean) throws IOException {
    threadPoolExecutor.execute(new runnable(userBean));
  }

  static class runnable implements Runnable {
    private final UserBean userBean;
    UserServiceImpl service = new UserServiceImpl();

    public runnable(UserBean userBean) throws IOException {
      this.userBean = userBean;
    }

    @Override
    public void run() {
      try {
        String loginUrl =
            "https://passport2-api.chaoxing.com/v11/loginregister?uname="
                + userBean.getTel()
                + "&code="
                + userBean.getPass()
                + "&loginType=1&roleSelect=true";
        Connection.Response response =
            Jsoup.connect(loginUrl).method(Connection.Method.POST).timeout(5000).execute();
        String s = response.parse().body().toString();
        if (s.contains("true")) {
          HashMap<String, String> cookies = (HashMap<String, String>) response.cookies();
          UserService service = new UserServiceImpl();
          userBean.setCookies(JsonUtil.mapToString(cookies));
          service.updateUser(userBean);
          System.out.println(userBean.getTel() + "密码" + userBean.getPass() + "已经更新cookie");
          EmailUtil.sendMail(
              userBean.getEmail(), userBean.getTel() + ":你的cookie貌似失效了,不过别慌,服务器成功为你更新cookie");
        } else {
          System.out.println(userBean.getTel() + "密码" + userBean.getPass() + "错误");
          EmailUtil.sendMail(
              userBean.getEmail(), userBean.getTel() + ":你的cookie貌似失效了,并且服务器更新失败.请确认你的云签到账号密码");
        }
        final ResultBean<String> deleteUser =
            service.deleteUser(userBean.getTel(), userBean.getPass());
        EmailUtil.sendMail(
            userBean.getEmail(),
            userBean.getTel()
                + "因为cookie失效,以及更新cookie失效已被删除,删除状态:"
                + deleteUser.getMsg()
                + "有疑问联系我哦");
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(
            userBean.getTel() + "密码" + userBean.getPass() + "错误" + e.getLocalizedMessage());
        EmailUtil.sendMail(
            userBean.getEmail(),
            userBean.getTel()
                + "密码"
                + userBean.getPass()
                + "错误"
                + e.getLocalizedMessage()
                + ":服务器更新cookie失效");
      }
    }
  }
}
