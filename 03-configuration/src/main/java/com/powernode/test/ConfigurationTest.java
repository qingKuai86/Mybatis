package com.powernode.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class ConfigurationTest {
    @Test
    public void testEnvironment() throws Exception {
        //获取SqLSessionFactory对象 (采用默认的方式获取)
        SqlSessionFactoryBuilder sqLSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //这种方式就是获取的默认环境
        SqlSessionFactory sqLSessionFactory = sqLSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));

        //这种方式就是通过环境id来使用指定的环境
        SqlSessionFactory sqLSessionFactory2 = sqLSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"), "testDB");

    }

}