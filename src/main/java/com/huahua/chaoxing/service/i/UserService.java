package com.huahua.chaoxing.service.i;

import java.util.HashMap;

import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.bean.UserBean;

import javax.servlet.http.Cookie;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Administrator
 */
public interface UserService {
    /**
     * @param tel       手机号
     * @param password  密码
     * @param signPlace 签到地点
     * @param email     邮箱
     * @param cookies   cookie信息
     * @return
     */
    ResultBean<String> login(BigInteger tel, String password, String signPlace, Cookie[] cookies, String email);


    /**
     * 删除一个用户
     * @param tel 手机号
     * @param pass
     * @return
     */
    ResultBean<String> deleteUser(BigInteger tel,String pass);

    /**
     * 返回所有用户信息
     *
     * @return
     */
    ArrayList<UserBean> getAll();


    /**
     * 判断用户是否存在
     *
     * @return
     */
    boolean checkUser(BigInteger tel, String pass);


    /**
     * 获得一个用户
     *
     * @return
     */
    UserBean getOneUser(BigInteger tel, String pass);

}
