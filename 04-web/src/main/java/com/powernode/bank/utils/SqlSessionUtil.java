package com.powernode.bank.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * MyBatis工具类
 *
 * @author 老杜
 * @version 1.0
 * @since 1.0
 */
public class SqlSessionUtil {
    private SqlSessionUtil(){}

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //全局的，服务器级别的，一个服务器当中定义一个即可。
    //为什么把SqlSession对象放到ThreadLocal当中呢?为了保证一个线程对应个SqlSession.
    private static ThreadLocal<SqlSession> local = new ThreadLocal<>();

    /**
     * 获取当前的会话，该会话支持自动提交。
     * @return 会话对象
     */
    public static SqlSession openSession() {
        SqlSession sqlSession = local.get() ;
        if (sqlSession == null) {
            sqlSession = sqlSessionFactory. openSession() ;
            //将sqLSession对象绑定到当前线程上。
            local.set(sqlSession);
        }
        return sqlSession;
    }
    /**
     * 关闭SqlSession对象
     * @param sqlSession
     */
    public static void close(SqlSession sqlSession){
        if (sqlSession != null) {
            sqlSession.close();
        }
        //注意移除SqlSession对象和当前线程的绑定关系。
        //因为Tomcat服务器支持线程池。也就是说:用过的t1线程对象中，可能下一次还会使用这个t1线程。
        local.remove();
    }

}