package com.huahua.chaoxing.servlet;

import com.google.gson.JsonParser;
import com.huahua.chaoxing.service.i.OneWordService;
import com.huahua.chaoxing.service.impl.OneWordServiceImpl;
import com.huahua.chaoxing.util.HttpUtil;
import com.huahua.chaoxing.util.JsonUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.jsoup.Jsoup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "OneWord", value = "/OneWord")
public class OneWord extends HttpServlet {

  private final OneWordService service = new OneWordServiceImpl();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    final ArrayList<String> word = service.getWord(10);
    response.getWriter().println(JsonUtil.objectToJson(word));
  }
}
