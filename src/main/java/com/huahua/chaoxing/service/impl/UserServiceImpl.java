package com.huahua.chaoxing.service.impl;

import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.bean.UserBean;
import com.huahua.chaoxing.mapper.UserMapper;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.util.EmailUtil;
import com.huahua.chaoxing.util.JsonUtil;
import com.huahua.chaoxing.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class UserServiceImpl implements UserService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);

    private HashMap<String, String> getHashMapByArray(Cookie[] cookies) {

        HashMap<String, String> map = new HashMap<>();
        for (Cookie cookie : cookies) {
            map.put(cookie.getName(), cookie.getValue());
        }
        return map;
    }

    /**
     * @param tel       手机号
     * @param password  密码
     * @param signPlace 签到地点
     * @param email
     * @return
     */
    @Override
    public ResultBean<String> login(BigInteger tel, String password, String signPlace, Cookie[] cookies, String email) {
        HashMap<String, String> cookie = getHashMapByArray(cookies);
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        UserBean userBean = new UserBean();
        userBean.setPass(password);
        userBean.setSignPlace(signPlace);
        userBean.setTel(tel);
        userBean.setEmail(email);
        userBean.setCookies(JsonUtil.mapToString(cookie));
        ResultBean<String> resultBean = new ResultBean<>();
        try {
            if (!checkUser(tel, password)) {
                mapper.insert(userBean);
                resultBean.setCode(200);
                resultBean.setMsg("添加成功");
                resultBean.setData("");
                EmailUtil.sendMail(userBean.getEmail(), "用户 ->>>>>" + userBean.getTel() + "\n 恭喜你提交成功 \n 记得一定要同步课程和图片 \n 不然服务器不会签到哦 \n 如果有时间记得去看看 \n 我只能尽量保证服务器的运行");
            } else {
                resultBean.setCode(201);
                resultBean.setMsg("已存在");
                resultBean.setData("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            sqlSession.rollback();
            resultBean.setCode(202);
            resultBean.setMsg("添加失败,请尝试更换邮箱或者手机号");
            resultBean.setData("");
        }
        sqlSession.commit();
        sqlSession.close();
        return resultBean;
    }

    /**
     * 删除一个用户
     *
     * @param tel  手机号
     * @param pass
     * @return
     */
    @Override
    public ResultBean<String> deleteUser(BigInteger tel, String pass) {
        ResultBean<String> resultBean = new ResultBean<>();
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        try {
            if (checkUser(tel, pass)) {
                mapper.delete(tel, pass);
                resultBean.setData("");
                resultBean.setMsg("删除成功");
                resultBean.setCode(200);
                return resultBean;
            }
            resultBean.setData("");
            resultBean.setMsg("用户不存在");
            resultBean.setCode(201);
            return resultBean;
        } catch (Exception e) {
            resultBean.setCode(202);
            resultBean.setData("");
            resultBean.setMsg("删除失败");
            sqlSession.rollback();
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
        return resultBean;
    }

    /**
     * 返回所有用户信息
     *
     * @return
     */
    @Override
    public ArrayList<UserBean> getAll() {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        ArrayList<UserBean> all = mapper.getAll();
        sqlSession.close();
        return all;
    }

    /**
     * 判断用户是否存在
     *
     * @param tel
     * @param pass
     * @return
     */
    @Override
    public boolean checkUser(BigInteger tel, String pass) {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        boolean b = mapper.checkUser(tel, pass) != 0;
        sqlSession.close();
        return b;
    }


    /**
     * 获得一个用户
     *
     * @param tel
     * @param pass
     * @return
     */
    @Override
    public UserBean getOneUser(BigInteger tel, String pass) {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        UserBean userBean = mapper.selectByTel(tel, pass);
        sqlSession.close();
        return userBean;
    }


}
