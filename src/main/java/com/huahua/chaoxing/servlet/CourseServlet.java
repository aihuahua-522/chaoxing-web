package com.huahua.chaoxing.servlet;

import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.service.i.CourseService;
import com.huahua.chaoxing.service.impl.CourseServiceImpl;
import com.huahua.chaoxing.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CourseServlet", value = "/CourseServlet")
public class CourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CourseService service = new CourseServiceImpl();
        ResultBean<String> resultBean = service.addCourse(request.getParameter("course"), request.getParameter("tel"), request.getParameter("pass"));
        response.getWriter().println(JsonUtil.objectToJson(resultBean));
    }
}
