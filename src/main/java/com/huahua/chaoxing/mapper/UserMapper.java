package com.huahua.chaoxing.mapper;

import com.huahua.chaoxing.bean.UserBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.ArrayList;

public interface UserMapper {
    /**
     * 根据手机号和密码删除一个用户
     *
     * @param tel
     * @return
     */
    int delete(@Param("tel") BigInteger tel, @Param("pass") String pass);


    /**
     * 添加一个用户
     *
     * @param userBean
     * @return
     */
    int insert(@Param("userBean") UserBean userBean);


    /**
     * 根据用户手机号和密码查询信息
     *
     * @param tel  手机号
     * @param pass 密码
     * @return
     */
    UserBean selectByTel(@Param("tel") BigInteger tel, @Param("pass") String pass);


    /**
     * 返回所有信息
     */
    ArrayList<UserBean> getAll();

    /**
     * 返回信息,不包括课程
     *
     * @return
     */
    int checkUser(@Param("tel") BigInteger tel, @Param("pass") String pass);


    /**
     * 更新用户
     * @param userBean
     */
    void update(@Param("userBean") UserBean userBean);
}