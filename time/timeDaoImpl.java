
import Time.pojo.time;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

/**
 * @author by wyl
 * @date 2021/9/28.20点25分
 */
public class timeDaoImpl extends DAO implements timeDao {

    @Override
    public int addTime(time t) {
        try {
            return UpdatePreparedStatement(getConnection(), "insert into jdbc.time value (?,?);", t.getId(), t.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<time> getTime(int id) {
        try {
            return getForListPreparedStatement(getConnection(), time.class, "select  * from jdbc.time where id=?", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateTime(time t) {
        try {
            return UpdatePreparedStatement(getConnection(), "update jdbc.time set time=? where id=?", t.getDate(), t.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteTime(int id) {
        try {
            return UpdatePreparedStatement(getConnection(), "delete from jdbc.time where id=?", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Test
    public void t1() {
        System.out.println(new timeDaoImpl().addTime(new time(1, new Date(new java.util.Date().getTime()))));
    }

    @Test
    public void t2() {
        new timeDaoImpl().getTime(1).forEach((v) -> System.out.println(v));
    }

    @Test
    public void t3() {
        System.out.println(new timeDaoImpl().updateTime(new time(1, new Date(new java.util.Date().getTime()))));
    }

    @Test
    public void t4() {
        System.out.println(new timeDaoImpl().deleteTime(1));
    }

}
