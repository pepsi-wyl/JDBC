

import java.util.Date;

/**
 * @author by wyl
 * @date 2021/9/28.19点59分
 */
public class time {

    private int id;
    private Date time;

    public time() {
    }

    public time(int id, Date time) {
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return time;
    }

    public void setDate(Date date) {
        this.time = date;
    }

    @Override
    public String toString() {
        return "time{" +
                "id=" + id +
                ", time=" + time +
                '}';
    }
}
