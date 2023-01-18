package com.powernode.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;


public class MyBatisIntroductionTest{
    @Test
    public void testMyBatisIntroductionTest() throws IOException {
        //获取SqSessionFactoryBuilder对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //获取SqLSessionFactory对象
        InputStream is = null; // Resources.getResourceAsStream默认就是从类的根路径下开始查找资源。
        try {
            is = Resources.getResourceAsStream ( "mybatis-config.xmL");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqLSessionFactory = sqlSessionFactoryBuilder.build(is);// 一般情况下都是一个数据库对应一个SqLSessionFactory对象。

        //获取SqLSession对象
        SqlSession sqlSession = sqLSessionFactory.openSession();//如果使用的事务管理器是JDBC的话， 底层实际上会执行: conn.setAutoCommit(false);
        //下面这种方式不建议，因为没有开启事务
        //SqlSession sqLSession = sqLSessionFactory.openSession(true);

        //执行SQL语句
        int count = sqlSession. insert( "insertCar"); // 返回值是影响数据库表当中的记录东数。
        System.out.println("插入了几条记录: "+ count);

        //手动提交
        sqlSession.commit();//如果使用的事务管理器是JDBC的话， 底层实际上还是会执行conn.commit();
        // 6. 关闭资源（只关闭是不会提交的）
        sqlSession.close();

    }
}
