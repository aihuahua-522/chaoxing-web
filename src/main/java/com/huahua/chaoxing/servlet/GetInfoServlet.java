package com.huahua.chaoxing.servlet;

import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@WebServlet(name = "GetInfoServlet", value = "/GetInfoServlet")
public class GetInfoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BigInteger tel = BigInteger.valueOf(Long.parseLong(request.getParameter("tel")));

        UserService service = new UserServiceImpl();
        UserBean pass = service.getOneUser(tel, request.getParameter("pass"));
        response.getWriter().println(JsonUtil.objectToJson(pass));
    }
}
