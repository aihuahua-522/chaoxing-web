package com.huahua.chaoxing.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huahua.chaoxing.bean.CourseBean;
import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.mapper.CourseMapper;
import com.huahua.chaoxing.service.i.CourseService;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;

public class CourseServiceImpl implements CourseService {
    private static final UserService userService = new UserServiceImpl();
    private static Logger logger = Logger.getLogger(CourseServiceImpl.class);

    /**
     * 添加课程
     *
     * @param courseBeans
     * @param pass
     * @return
     */
    @Override
    public ResultBean<String> addCourse(String courseBeans, String tel, String pass) {
        Type type = new TypeToken<ArrayList<CourseBean>>() {
        }.getType();
        ArrayList<CourseBean> list = new Gson().fromJson(courseBeans, type);
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);
        ResultBean<String> resultBean = new ResultBean<>();
        try {
            // 判断用户存在
            if (userService.checkUser(BigInteger.valueOf(Long.parseLong(tel)), pass)) {
                // 先清空
                mapper.deleteAllCourse(BigInteger.valueOf(Long.parseLong(tel)));
                for (CourseBean courseBean : list) {
                    mapper.insertCourse(courseBean, BigInteger.valueOf(Long.parseLong(tel)));
                }
                resultBean.setCode(200);
                resultBean.setMsg("提交成功");
                resultBean.setData("");
                return resultBean;
            }
            resultBean.setCode(201);
            resultBean.setData("");
            resultBean.setMsg("账号或密码错误");
            return resultBean;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            sqlSession.rollback();
            resultBean.setCode(202);
            resultBean.setData("");
            resultBean.setMsg("提交失败" + e.getLocalizedMessage());
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
        return resultBean;
    }

    /**
     * 删除所有课程
     *
     * @param tel
     * @param pass
     * @return
     */
    @Override
    public ResultBean<String> deleteAllCourse(String tel, String pass) {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);

        ResultBean<String> resultBean = new ResultBean<>();
        try {
            if (userService.checkUser(BigInteger.valueOf(Long.parseLong(tel)), pass)) {
                mapper.deleteAllCourse(BigInteger.valueOf(Long.parseLong(tel)));
                resultBean.setCode(200);
                resultBean.setData("");
                resultBean.setMsg("删除成功");
                return resultBean;
            }
            resultBean.setCode(201);
            resultBean.setData("");
            resultBean.setMsg("账号或密码错误");
            return resultBean;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            sqlSession.rollback();
            resultBean.setCode(200);
            resultBean.setData("");
            resultBean.setMsg("删除成功");
            return resultBean;
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }

}
