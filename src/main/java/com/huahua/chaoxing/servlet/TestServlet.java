package com.huahua.chaoxing.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@WebServlet(name = "TestServlet",value = "/test")
public class TestServlet extends HttpServlet {
    static ArrayList<String> strings = new ArrayList<>();
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
        strings.add("huahua10@jxwazx.cn");
    }

    public static String getEmail() {
        return strings.get(new Random().nextInt(strings.size()));
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(getEmail());
    }
}
