package com.huahua.chaoxing.service.i;

import com.huahua.chaoxing.bean.ResultBean;

import java.math.BigInteger;

/**
 * @author Administrator
 */
public interface PicService {

    /**
     * 设置签到图片
     *
     * @param picBeans 签到图片数据集合
     * @return
     */
    ResultBean<String> setPic(BigInteger tel, String pass, String picBeans);


    /**
     * 删除指定图片
     *
     * @param tel      号码
     * @param objectId 图片id
     * @return
     */
    ResultBean<String> deletePic(BigInteger tel, String objectId);


    /**
     * 删除所有图片
     *
     * @param tel 号码
     * @return
     */
    ResultBean<String> deleteAllPic(BigInteger tel, String pass);


    /**
     * 获取签到图片
     *
     * @param tel 电话
     * @return
     */
    ResultBean<String> getPic(BigInteger tel);


}
