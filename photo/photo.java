package photo;

import java.io.InputStream;

/**
 * @author by wyl
 * @date 2021/9/28.18点27分
 */
public class photo {

    private int id;
    private String name;
    private InputStream in;

    public photo() {
    }

    public photo(int id) {
        this.id = id;
    }

    public photo(int id, String name, InputStream in) {
        this.id = id;
        this.name = name;
        this.in = in;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

}
