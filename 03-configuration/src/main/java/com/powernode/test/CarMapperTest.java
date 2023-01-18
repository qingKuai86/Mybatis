package com.powernode.test;

import com.powernode.pojo.Car;
import com.powernode.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarMapperTest {

    @Test
    public void testNamespace(){
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL语句
        List<Object> cars = sqlSession.selectList("car.selectCarAll");//注意加上命名空间
        // 输出结果
        cars.forEach(car -> System.out.println(car));
    }

    @Test
    public void testSelectCarById(){
        SqlSession sqlSession = SqlSessionUtil.openSession();

        //执行DQL语句。查询。根据id查询。返回结果一定是一条。
        // mybatis底层执行了select语句之后，: 定会返回一个结果集对象: ResultSet
        // JDBC中叫做ResultSet, 接下来就是mybatis应该从ResultSet中取出数据，封装java对象。
        Object car = sqlSession.selectOne("selectCarById", 1);
        System.out.println(car);
        sqlSession.close();
    }

    @Test
    public void testSelectCarAll(){
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL语句
        List<Object> cars = sqlSession.selectList("selectCarAll");
        // 输出结果
        cars.forEach(car -> System.out.println(car));
        sqlSession.close();
    }

    @Test
    public void testUpdateCarByPOJO(){
        // 准备数据
        Car car = new Car();
        car.setId(10L);
        car.setCarNum("102");
        car.setBrand("比亚迪汉");
        car.setGuidePrice(30.23);
        car.setProduceTime("2018-09-10");
        car.setCarType("电车");
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL语句
        int count = sqlSession.update("updateCarByPOJO", car);
        System.out.println("更新了几条记录：" + count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testDeleteByCarNum(){
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL语句
        int count = sqlSession.delete("deleteByCarNum", "13");
        System.out.println("删除了几条记录：" + count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testInsertCarByPojo(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        Car car = new Car();
        car.setCarNum("103");
        car.setBrand("奔驰C200");
        car.setGuidePrice(33.23);
        car.setProduceTime("2020-10-11");
        car.setCarType("燃油车");

        int count = sqlSession. insert("insertCarByPojo", car);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();

    }

    @Test
    public void testInsertCar(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        //前端传过来数据」。
        //这个对象我们先使用Map集合进行教据的封装。
        Map<String, Object> map = new HashMap<>();
        // 准备数据
        map.put("carNum", "103");
        map.put("brand", "奔驰E300L");
        map.put("guidePrice", 50.3);
        map.put("produceTime", "2020-10-01");
        map.put("carType", "新能源车");
        // 执行SQL语白
        // insert方法的爹数:
        //第一个参数: sqlId, 从CarMapper.xml文件中复制。
        //第二个参数:封装数据的对象。
        int count = sqlSession. insert("insertCar", map);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();

    }
}
