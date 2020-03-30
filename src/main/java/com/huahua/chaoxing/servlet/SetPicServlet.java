package com.huahua.chaoxing.servlet;

import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.service.i.PicService;
import com.huahua.chaoxing.service.impl.PicServiceImpl;
import com.huahua.chaoxing.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@WebServlet(name = "签到图片上传", value = "/SetPicServlet")
public class SetPicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String picBeans = request.getParameter("picBeans");
        String tel = request.getParameter("tel");
        String pass = request.getParameter("pass");

        PicService picService = new PicServiceImpl();
        ResultBean<String> result = picService.setPic(BigInteger.valueOf(Long.parseLong(tel)), pass, picBeans);

        response.getWriter().write(JsonUtil.objectToJson(result));

    }
}
