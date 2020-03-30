package com.huahua.chaoxing.mapper;

import com.huahua.chaoxing.bean.PicBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

public interface PicMapper {

    /**
     * 插入图片
     * @param tel 手机号
     * @param objectId
     */
    void insert (@Param("tel") BigInteger tel , @Param("objectId") String objectId);


    /**
     * 删除所有
     * @param tel 电话号码
     */
    void deleteAllPic(@Param("tel") BigInteger tel);

}