import com.powernode.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class CarMapperTest {

    @Test
    public void testInsertCar(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL
        int count = sqlSession.insert("insertCar");
        System.out.println("插入了几条记录:" + count);
        sqlSession.commit();
        sqlSession.close();
    }
}
