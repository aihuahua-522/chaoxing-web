package com.huahua.chaoxing.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huahua.chaoxing.bean.PicBean;
import com.huahua.chaoxing.bean.ResultBean;
import com.huahua.chaoxing.mapper.PicMapper;
import com.huahua.chaoxing.service.i.PicService;
import com.huahua.chaoxing.service.i.UserService;
import com.huahua.chaoxing.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;

public class PicServiceImpl implements PicService {
    private UserService userService = new UserServiceImpl();

    /**
     * 设置签到图片
     *
     * @param picBeans 签到图片数据集合
     * @return
     */
    @Override
    public ResultBean<String> setPic(BigInteger tel, String pass, String picBeans) {

        SqlSession sqlSession = MybatisUtil.getSqlSession();
        PicMapper mapper = sqlSession.getMapper(PicMapper.class);
        ResultBean<String> resultBean = new ResultBean<>();
        Type type = new TypeToken<ArrayList<PicBean>>() {
        }.getType();
        try {
            ArrayList<PicBean> picBeanList = new Gson().fromJson(picBeans, type);
            if (userService.checkUser(tel, pass)) {
                mapper.deleteAllPic(tel);
                for (PicBean picBean : picBeanList) {
                    mapper.insert(tel, picBean.getObjectid());
                }
                resultBean.setCode(200);
                resultBean.setMsg("图片已上传");
                resultBean.setData("");
            } else {
                resultBean.setCode(201);
                resultBean.setMsg("用户名或密码错误");
                resultBean.setData("");
            }
        } catch (Exception e) {
            sqlSession.rollback();
            resultBean.setCode(201);
            resultBean.setMsg("上传失败");
            e.printStackTrace();
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
        return resultBean;
    }


    /**
     * 删除指定图片
     *
     * @param tel      号码
     * @param objectId 图片id
     * @return
     */
    @Override
    public ResultBean<String> deletePic(BigInteger tel, String objectId) {

        // TODO 留坑
        return null;
    }

    /**
     * 删除所有图片
     *
     * @param tel 号码
     * @return
     */
    @Override
    public ResultBean<String> deleteAllPic(BigInteger tel, String pass) {

        SqlSession sqlSession = MybatisUtil.getSqlSession();
        PicMapper mapper = sqlSession.getMapper(PicMapper.class);
        ResultBean<String> resultBean = new ResultBean<>();
        try {
            if (userService.checkUser(tel, pass)) {
                mapper.deleteAllPic(tel);
                resultBean.setCode(200);
                resultBean.setMsg("图片删除成功");
                resultBean.setData("");
            } else {
                resultBean.setCode(201);
                resultBean.setMsg("账号密码错误");
                resultBean.setData("");
            }
        } catch (Exception e) {
            sqlSession.rollback();
            resultBean.setCode(201);
            resultBean.setMsg("图片删除失败");
            resultBean.setData("");
            e.printStackTrace();
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
        return resultBean;
    }

    /**
     * 获取签到图片
     *
     * @param tel 电话
     * @return
     */
    @Override
    public ResultBean<String> getPic(BigInteger tel) {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        PicMapper mapper = sqlSession.getMapper(PicMapper.class);
        // TODO 留坑
        return null;
    }
}
