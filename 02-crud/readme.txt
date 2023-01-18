使用mybatis完成CRUD操作

1.什么是CRUD
    C: Create 增
    R: Retrieve查 (检索)
    U: Update 改
    D: Delete删

2. insert
    <insert id= ”insertCar">
    insert into t_ car(id, car num , brand, guide_ price, produce_ time , car_ type)
    values(null, '1003','丰田霸道' ,30.0, '2000-10-11'，'燃油车);
    </insert>
    这样写的问题是?
        数值显然是写死到配置文件中的。
        这个在实际开发中是不存在的。
        一定是前端的form表单提交过来数据。然后将值传给sql语句。
    例如: JDBC 的代码是怎么写的2
        String sqL = "insert into t_car(id, car _num, brand, guide . price , produce_ time ,car_type) values(null,?,?,?,?,?)";
        ps. setString(1, xxx);
        ps. setString(2, yyy);
        ....

在JDBC当中占位符采用的是?，在mybatis当中是什么呢?
    和?等效的写法是: #{}
    在mybatis当中不能使用?占位符，必须使用#{}来代替JDBC当中的?
    #{} 和JDBC当中的 ? 是等效的。

java程序中使用Map可以给SQL语句的占位符传值:

      Map<String, 0bject> map = new HashMap<>();
      map.put("k1", "103");
      map.put("k2", "奔驰E300L");
      map.put("k3", 50.3);
      map.put("k4", "2020-10-01");
      map.put("k5", "新能源车");

      insert into t.car(id, car_num, brand, guide_price,produce_time,car_type) values(null, #{k1}, #{k2}, #{k3}, #{k4} , #{k5});
      注意: #{这里写什么?写map集合的key,如果key不存在，获取的是null}

    !!!一般map集合的key起名的时候要见名知意。
        map.put("carNum", "103");
        map.put("brand", "奔驰E300L");
        map.put("guidePrice", 50.3);
        map.put("produceTime", "2020-10-01");
        map.put("carType", "新能源车");

        insert into t_car(id,car_num,brand,guide_price,produce_time,car_type)
        values (null, #{carNum}, #{brand}, #{guidePrice}, #{produceTime}, #{carType})


    java程序中使用P0J0类给SQL语句的占位符传值:
        Car car = new Car(null, "3333"， "比亚迪秦"，30.0， "2020-11-11", “新能源");
        注意:占位符#{},大括号里面写: pojo类的属性名
        insert into t_ car(id, car_ num, brand, guide_ price ，produce_ time ，car_ type)
        values(nuLl, #{}, #{}, #{}, #{}, #{})

    把SQL语句写成这个德行:
        insert into t_car(id, car. num, brand, guide_price , produce_time, car_type )
        values (nuLl, #{xyz} , #{brand} , #{guidePrice}，#{produceTime}, #{carType})
    出现了什么问题呢?
        There is no getter for property named 'xyz' in ' class com.powernode.pojo.Car '
        mybatis去找: Car类中的getXyz()方法去了。没找到，报错了。

    通过这个测试，得出一个结论:
        严格意义上来说:如果使用P0J0对象传递值的话，#{}这 个大括号中到底写什么?
        写的是get方法的方法名去掉get,然后将剩下的单词首字母小写，然后放进去。

        例如: getUsername() --> #{username}
        例如: getEmail() --> #{email}
    也就是说mybatis在底层给?传值的时候，先要获取值，怎么获取的?
    调用了pojo对象的get方法。例如: car.getCarNum(), car.getCarType(), car.getBrand()

3.delete
    *需求:根据id删除数据
    将id=59的数据删除。
    实现:
    int count = sqlSession.delete("deleteById", 59);
    <delete id=" deleteById">
    delete from t_car where id = #f{fdsfd}
    </deLete>
    注意:如果占位符只有一个，那么#{}的大括号里可以随意。但是最好见名知意。

4. update
    *需求:根据id修改某条记录。
    实现:
        <update id="updateCarByPOJO">
                update t_car set
                car_num = #{carNum}, brand = #{brand},
                guide_price = #{guidePrice}, produce_time = #{produceTime},
                car_type = #{carType}
                where id = #{id}
        </update>
    Car car = new Car(4L, "9999"， "凯关瑞"， 30.3, "1999- 11-10"， "燃油车");
    int count = sqlSession. update("updateById", car);

5. seLect (查一个，根据主键查询的话，返回的结果定是个。)
    *需求:根据id查询。
    实现:
        <select id="selectById" resultType="com.powernode.pojo.Car">
        select * from t_car where id = #{id}
        </select>
        Object car = sqLSession. select0ne(" selectById", 1);
    需要特别注意的是:
        select标签中resultType属性，这个属性用来告诉mybatis,查询结果集封装成什么类型的java对象。你需费告诉mybatis.
        resultType通常写的是:全限定类名。

    输出：Car{id=1, carNum= 'null', brand='宝 马520Li', guidePrice=null, produceTime='null', carType='nuLl'}
        输出结果有点不对劲:
            id和brand属性有值。
            其他属性为nuLl。
    carNum以及其他的这几个属性没有赋上值的原因是什么?
        mysql> select * from t_car where id = 1;
        执行结果:
        +----+---------+-----------+-------------+--------------+----------+
        | id | car_num | brand     | guide_price | produce_time | car_type |
        +----+---------+-----------+-------------+--------------+----------+
        |  1 | 1001    | 宝马520Li |       41.00 | 2022-09-10   | 燃油车   |
        +----+---------+-----------+-------------+--------------+----------+

        car_num、guide_price、produce_time、 car_type这是查询结果的列名。
        这些列名和Car类中的属性名对不上。
        Car类的属性名:
            carNum、guidePrice、 produceTime、 carType
        那这个问题怎么解决呢?
        方法：
            1.建表时列名和Car类中的属性名一致（一般不用）
            2.起别名
            seLect语句查询的时候，查询结果集的列名是可以使用as关键字起别名的。

            <select id="selectCarById" resultType="com.powernode.pojo.Car">
                select
                    id, car_num as carNum, brand, guide_price as guidePrice, produce_time as produceTime, car_type as carType
                from t_car where id = #{id}
            </select>
            3.其它

6. select (查所有的)
    <select id=" selectALL" resultType= ”com.powernode.pojo.Car">
    seLect
        id,car num as carNum, brand, guide_ price as guidePrice 1
        produce_ time as produceTime ,
        car_ type as carType
    from t_ car
    </select>

    List<Object> cars = sqLSession.selectList("selectALL" );

    注意: resultType还是指定要封装的结果集的类型。不是指定List类型，是指定List集合中元素的类型。
    selectList方法: mybatis通过这个方法就可以得知你需要一个List集合。它会自动给你返回一个List集合。

7.在sqlMapper.xmL文件当中有一个namespace,这个属性是用来指定命名空间的,用来防止id重复。
怎么用?
    在xmL文件中:
    <mapper namespace= "aaaa">
    <select id=" selectAlL" resultType= ”com powernode.pojo.Car">
    select
        id,car_nUm as carNum, brand, guide_price as guidePrice,
        produce_time as produceTime,
        car_type as carType
    from t_car
    </select>
    </mapper>

在java程序中的写法:
    List<Object> cars = sqlSession.selectList("aaaa.selectAll);

    所以实际上本质上，mybatis中的sqlId的完整写法:
        namespace.id
