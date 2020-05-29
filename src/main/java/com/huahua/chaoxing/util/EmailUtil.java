package com.huahua.chaoxing.util;

import com.huahua.chaoxing.bean.qqBean;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * 提供邮件发送服务
 */
public class EmailUtil {
    public static void sendMail(String toEmail, String context) {
        String qq = toEmail.split("@qq.com")[0];
        qqBean qqBean = new qqBean();
        qqBean.setUser_id(qq);
        qqBean.setMessage(DateUtil.getTime() + "\n" + context + "\n" + "小广告:公众号,会教所有我会的");
        try {
            HttpUtil.trustEveryone();
            Jsoup.connect("http://39.96.95.173:5700/send_private_msg")
                    .requestBody(JsonUtil.objectToJson(qqBean))
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .post();
            System.out.println(JsonUtil.objectToJson(qqBean));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}