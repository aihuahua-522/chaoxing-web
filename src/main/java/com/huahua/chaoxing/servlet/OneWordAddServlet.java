package com.huahua.chaoxing.servlet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huahua.chaoxing.service.i.OneWordService;
import com.huahua.chaoxing.service.impl.OneWordServiceImpl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "爬虫获取二次元", value = "/OneWordAddServlet")
public class OneWordAddServlet extends HttpServlet {
  private static final OneWordService service = new OneWordServiceImpl();

  private static final ExecutorService executorService2 =
      new ThreadPoolExecutor(20, 100, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    for (int i = 0; i < 10000; i++) {
      executorService2.execute(this::addOneWord);
    }
  }

  public void addOneWord() {
    final OkHttpClient client = new OkHttpClient();
    final Request request1 =
        new Request.Builder()
            .url("https://api.abcyun.co/api/tool/yiyan/token/5d8f31cf6a8ab")
            .get()
            .build();
    final Call call = client.newCall(request1);
    try {
      final Response response1 = call.execute();
      final String string = response1.body().string();
      final JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();
      final String hitokoto = jsonObject.get("hitokoto").getAsString();
      final String from = jsonObject.get("from").getAsString();
      service.save(hitokoto + "-- 【" + from + "】");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
