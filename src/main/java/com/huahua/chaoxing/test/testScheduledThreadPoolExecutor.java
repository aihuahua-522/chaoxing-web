package com.huahua.chaoxing.test;

import com.sun.deploy.appcontext.AppContext;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class testScheduledThreadPoolExecutor {

    ExecutorService executorService = new ThreadPoolExecutor(500,
            Integer.MAX_VALUE,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(10),
            r -> new Thread(r, "爱花花"));

    @Test
    public void test() throws ExecutionException, InterruptedException {
        testRunnable testRunnable = new testRunnable();
        for (int i = 0; i < 100000; i++) {
            Future<?> submit = executorService.submit(testRunnable);
//            executorService.execute();
            System.out.println(i);
        }
    }

    class testRunnable implements Runnable {

        @Override
        public void run() {
            String format = new SimpleDateFormat("HH:mm:ss ").format(new Date());
            System.out.println(Thread.currentThread().getName() + "线程运行" + format);
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("运行结束");
        }
    }


}
