package com.huahua.chaoxing.servlet;

import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.service.impl.UserServiceImpl;
import com.huahua.chaoxing.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.math.BigInteger;

@WebServlet(name = "DeleteServlet", value = "/DeleteServlet")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BigInteger tel = BigInteger.valueOf(Long.parseLong(request.getParameter("tel").replaceAll("\\D", "")));
        String pass = request.getParameter("pass");
        UserService service = new UserServiceImpl();
        ResultBean<String> resultBean = service.deleteUser(tel, pass);
        response.getWriter().println(JsonUtil.objectToJson(resultBean));
    }
}
