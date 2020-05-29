package com.huahua.chaoxing.test;

import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.util.CookiesUtil;
import com.huahua.chaoxing.util.DateUtil;
import com.huahua.chaoxing.util.EmailUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;

public class timeTest {

    @Test
    public void getTime() throws IOException, InterruptedException {
        BigInteger tel = BigInteger.valueOf(Long.parseLong("17707962579"));
        final UserBean userBean = new UserBean(tel,"1234567890");
        CookiesUtil.refreshCookies(userBean);
        Thread.sleep(30000);
    }


}
