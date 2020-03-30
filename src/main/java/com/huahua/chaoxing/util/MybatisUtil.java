package com.huahua.chaoxing.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MybatisUtil {

    private static SqlSessionFactory factory;

    /**
     * 获取SqlSessionFactory
     *
     * @return SqlSessionFactory
     */
    static {
        String resource = "mybatis-config.xml";
        InputStream is = MybatisUtil.class.getClassLoader().getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(is);
    }

    /**
     * 获取SqlSession
     *
     * @return SqlSession
     */
    public synchronized static SqlSession getSqlSession() {
        return factory.openSession();
    }

    /**
     * 获取SqlSession
     *
     * @param isAutoCommit true 表示创建的SqlSession对象在执行完SQL之后会自动提交事务
     *                     false 表示创建的SqlSession对象在执行完SQL之后不会自动提交事务，这时就需要我们手动调用sqlSession.commit()提交事务
     * @return SqlSession
     */
    public synchronized static SqlSession getSqlSession(boolean isAutoCommit) {
        return factory.openSession(isAutoCommit);
    }
}