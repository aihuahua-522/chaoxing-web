package com.huahua.chaoxing.servlet;

import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@WebServlet(value = "/LoginServlet", name = "账号上传")
public class LoginServlet extends HttpServlet {

    private UserService service = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String tel = request.getParameter("tel");
        String pass = request.getParameter("pass");
        String signPlace = request.getParameter("signPlace");
        String email = request.getParameter("email");
        Cookie[] cookies = request.getCookies();
        tel = tel.replaceAll("\\D","");
        ResultBean<String> resultBean = service.login(BigInteger.valueOf(Long.parseLong(tel)), pass, signPlace, cookies, email);
        response.getWriter().println(JsonUtil.objectToJson(resultBean));
    }

}
