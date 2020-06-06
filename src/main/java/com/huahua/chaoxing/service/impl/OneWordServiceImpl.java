package com.huahua.chaoxing.service.impl;

import com.huahua.chaoxing.mapper.OneWordMapper;
import com.huahua.chaoxing.service.i.OneWordService;
import com.huahua.chaoxing.util.MybatisUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

public class OneWordServiceImpl implements OneWordService {

  /**
   * 保存
   *
   * @param word
   */
  @Override
  public void save(String word) {

    try (SqlSession sqlSession = MybatisUtil.getSqlSession()) {
      OneWordMapper mapper = sqlSession.getMapper(OneWordMapper.class);
      mapper.save(word);
      sqlSession.commit();
    }
  }

  /**
   * 获取随机语录
   *
   * @param size 条数
   * @return
   */
  @Override
  public ArrayList<String> getWord(int size) {
    try (SqlSession sqlSession = MybatisUtil.getSqlSession()) {
      OneWordMapper mapper = sqlSession.getMapper(OneWordMapper.class);
      return mapper.getWord(size);
    }
  }
}
