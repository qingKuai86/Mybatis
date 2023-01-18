package com.powernode.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * MyBatis工具类
 *
 * @author 老杜
 * @version 1.0
 * @since 1.0
 */
public class SqlSessionUtil {
    // 工具类的构造方法一般都是私有化的。
    // 工具类中所有的方法都是静态的，直接采用类名即可调用。不需要new对象。
    // 为了防止new对象，构造方法私有化
    private SqlSessionUtil(){}

    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 类加载时初始化sqlSessionFactory对象，只加载一次
     * 一个SqlSessionFactory对应一个environment，一个environment通常是一个数据库，所以我们只初始化一次即可
     * SqLSessionUtil 工具类在进行第一次加载的时候，
     * 解析mybatis-config.xml文件,创建SqLSessionFactory对象。
     */
    static {
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每调用一次openSession()可获取一个新的会话，该会话支持自动提交。
     * * @return 新的会话对象
     */
    public static SqlSession openSession() {
        //开启事务，手动提交
        return sqlSessionFactory.openSession();
        //不开启事务，自动提交
        //return sqlSessionFactory.openSession(true);
    }

    //没必要每次都new一个sqlSessionFactory，因此采用上面这种做法
/*    public static SqlSession openSession(){
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
   //sqLSessionFactory对象: 一个SqLSessionFactory对应一个environment, 一个environment通常是一个数据库。
        SqlSessionFactory sqlSessionFactory = null;
        try {
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }*/

}