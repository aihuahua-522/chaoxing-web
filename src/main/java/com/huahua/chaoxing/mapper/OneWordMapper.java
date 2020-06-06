package com.huahua.chaoxing.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface OneWordMapper {

    void save(@Param("word") String word);

    ArrayList<String> getWord(@Param("size") int size);
}


