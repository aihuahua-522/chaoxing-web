package com.huahua.chaoxing.util;

import com.huahua.chaoxing.bean.Authentication;
import com.huahua.chaoxing.bean.qqBean;
import com.sun.mail.util.MailSSLSocketFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * 提供邮件发送服务
 */
public class EmailUtil {
    static ArrayList<String> strings = new ArrayList<>();
/*

    static String HOST = "smtp.163.com"; // smtp服务器
    static String FROM = "lovehuahua522@163.com"; // 发件人地址
    static String USER = "lovehuahua522@163.com"; // 用户名
    static String PWD = "HAWWQNSZTBLIPSCS"; // 163的授权码
    static String SUBJECT = "爱花花运行日志"; // 邮件标题

    */

    /**
     * 24      * 发送邮件
     * 25      * @param host
     * 26      * @param user
     * 27      * @param pwd
     * 28
     *//*

    public static void sendMail(String toEmail, String context) {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.auth", "true");  //需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        Session session = Session.getDefaultInstance(props);//用props对象构建一个session
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);//用session为参数定义消息对象
        try {
            message.setFrom(new InternetAddress(FROM));// 加载发件人地址
            InternetAddress sendTo = new InternetAddress(toEmail); // 加载收件人地址

            message.addRecipients(Message.RecipientType.TO, String.valueOf(sendTo));
            message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(FROM));//设置在发送给收信人之前给自己（发送方）抄送一份，不然会被当成垃圾邮件，报554错
            message.setSubject(SUBJECT);//加载标题
            Multipart multipart = new MimeMultipart();//向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            BodyPart contentPart = new MimeBodyPart();//设置邮件的文本内容
            contentPart.setText(context);
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);//将multipart对象放到message中
            message.saveChanges(); //保存邮件
            Transport transport = session.getTransport("smtp");//发送邮件
            transport.connect(HOST, USER, PWD);//连接服务器的邮箱
            transport.sendMessage(message, message.getAllRecipients());//把邮件发送出去
            transport.close();//关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    static {
        strings.add("huahua@jxwazx.cn");
        strings.add("huahua1@jxwazx.cn");
        strings.add("huahua2@jxwazx.cn");
        strings.add("huahua3@jxwazx.cn");
        strings.add("huahua4@jxwazx.cn");
        strings.add("huahua5@jxwazx.cn");
        strings.add("huahua6@jxwazx.cn");
        strings.add("huahua7@jxwazx.cn");
        strings.add("huahua8@jxwazx.cn");
        strings.add("huahua9@jxwazx.cn");
        strings.add("huahua11@jxwazx.cn");
        strings.add("huahua12@jxwazx.cn");
        strings.add("huahua13@jxwazx.cn");
        strings.add("huahua14@jxwazx.cn");
        strings.add("huahua15@jxwazx.cn");
        strings.add("huahua16@jxwazx.cn");
        strings.add("huahua17@jxwazx.cn");
        strings.add("huahua18@jxwazx.cn");
        strings.add("huahua19@jxwazx.cn");
        strings.add("huahua20@jxwazx.cn");
    }

    public static String getEmail() {
        return strings.get(new Random().nextInt(strings.size()));
    }

    public static void sendMail(String toEmail, String context) {
        String username = getEmail();
        String qq = toEmail.split("@qq.com")[0];
        System.out.println("qq" + qq);
        qqBean qqBean = new qqBean();
        qqBean.setUser_id(qq);
        qqBean.setMessage(DateUtil.getTime() + "\n" + context);
        try {
            HttpUtil.trustEveryone();
            Document post = Jsoup.connect("http://39.108.127.153:5700/send_private_msg")
                    .requestBody(JsonUtil.objectToJson(qqBean))
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .post();
            System.out.println(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(username);
            Properties prop = new Properties();
            //协议
            prop.setProperty("mail.transport.protocol", "smtp");
            //服务器
            prop.setProperty("mail.smtp.host", "smtp.ym.163.com");
            //端口
            prop.setProperty("mail.smtp.port", "994");
            //使用smtp身份验证
            prop.setProperty("mail.smtp.auth", "true");
            //使用SSL，企业邮箱必需！
            //开启安全协议
            MailSSLSocketFactory sf = null;
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);
            Authentication authentication = new Authentication(username, "1234567890");
            //
            //获取Session对象
            Session s = Session.getDefaultInstance(prop, authentication);
            //设置session的调试模式，发布时取消
            s.setDebug(true);
            MimeMessage mimeMessage = new MimeMessage(s);
            mimeMessage.setFrom(new InternetAddress(username, "花花"));
            mimeMessage.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse("huahua522@aliyun.com"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            //设置主题
            mimeMessage.setSubject("花花日志");
            mimeMessage.setSentDate(new Date());
            //设置内容
            mimeMessage.setText(DateUtil.getTime() + "\n" + context);
            mimeMessage.saveChanges();
            //发送
            Transport.send(mimeMessage);
        } catch (GeneralSecurityException | MessagingException | UnsupportedEncodingException e) {
            System.out.println("异常账号" + username);
            e.printStackTrace();
        }

    }


    public static class emailSendRunning implements Runnable {
        private String email;
        private String text;

        public emailSendRunning(String email, String text) {
            this.email = email;
            this.text = text;
        }

        @Override
        public void run() {
            try {
                sendMail(email, text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}