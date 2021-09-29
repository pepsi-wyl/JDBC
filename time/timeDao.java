
import Time.pojo.time;

import java.util.List;

/**
 * @author by wyl
 * @date 2021/9/28.时间
 */
public interface timeDao {

    /**
     * 插入时间
     */
    int addTime(time t);

    /**
     * 查询时间
     */
    List<time> getTime(int id);

    /**
     * 修改时间
     */
    int updateTime(time t);

    /**
     * 删除时间
     */
    int deleteTime(int id);


}
