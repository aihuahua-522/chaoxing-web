package com.huahua.chaoxing.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huahua.chaoxing.service.i.OneWordService;
import com.huahua.chaoxing.service.impl.OneWordServiceImpl;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.*;

public class TestOneWord {

  private static final OneWordService service = new OneWordServiceImpl();
  private static final ScheduledExecutorService executorService =
      new ScheduledThreadPoolExecutor(20);
  private static final ExecutorService executorService2 =
      new ThreadPoolExecutor(20, 100, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

  public static void main(String[] args) {
    try {
      for (int i = 0; i < 10000; i++) {
        executorService2.execute(TestOneWord::getOneWord2);
      }

    } catch (Exception e) {
    }
  }

  @Test
  public static void getOneWord() {
    final OkHttpClient client = new OkHttpClient();
    final Request request =
        new Request.Builder().url("https://nigansha.com/api/json?t=&n=3").get().build();
    final Call call = client.newCall(request);
    try {
      final Response response = call.execute();
      final String string = response.body().string();
      final String asString =
          JsonParser.parseString(string)
              .getAsJsonObject()
              .get("result")
              .getAsJsonArray()
              .get(0)
              .getAsJsonObject()
              .get("txt")
              .getAsString();
      //            System.out.println(string);
      System.out.println(asString);
      service.save(asString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public static void getOneWord2() {
    final OkHttpClient client = new OkHttpClient();
    final Request request =
        new Request.Builder()
            .url("https://api.abcyun.co/api/tool/yiyan/token/5d8f31cf6a8ab")
            .get()
            .build();
    final Call call = client.newCall(request);
    try {
      final Response response = call.execute();
      final String string = response.body().string();
      final JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();
      final String hitokoto = jsonObject.get("hitokoto").getAsString();
      final String from = jsonObject.get("from").getAsString();
      service.save(hitokoto + "-- 【" + from + "】");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
