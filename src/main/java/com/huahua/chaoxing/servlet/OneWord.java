package com.huahua.chaoxing.servlet;

import com.google.gson.JsonParser;
import com.huahua.chaoxing.util.HttpUtil;
import com.huahua.chaoxing.util.JsonUtil;
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HttpUtil.trustEveryone();
            String string = Jsoup.connect("https://nigansha.com/api/json?t=&n=3").ignoreContentType(true).ignoreHttpErrors(true).get().text();
            String asString = JsonParser.parseString(string).getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject().get("txt").getAsString();
            strings.add(asString);
        }
        response.getWriter().println(JsonUtil.objectToJson(strings));
    }
}
