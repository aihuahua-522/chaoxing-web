package com.huahua.chaoxing.test;

import com.huahua.chaoxing.util.DateUtil;
import com.huahua.chaoxing.util.EmailUtil;
import org.junit.jupiter.api.Test;

public class timeTest {

    @Test
    public void getTime() {
        System.out.println(DateUtil.getTime());
    }

    @Test
    public void test() {
        System.out.println(new EmailUtil.emailSendRunning("", ""));
    }

}
