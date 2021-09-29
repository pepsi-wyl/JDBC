package photo;

import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author by wyl
 * @date 2021/9/28.20点32分
 */

public class ImageDemo extends DAO {

    // 读取本地图片获取输入流
    public static FileInputStream readImage(String path) throws IOException {
        return new FileInputStream(path);
    }

    // 读取表中图片获取输出流
    public static void readBinImage(InputStream in, String targetPath) {

        File file = new File(targetPath);
        try {
            file.createNewFile();     //创建一个新文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);    //写出数据
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();    //关闭文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test                            // 将图片插入数据库
    public void test() {


        Connection conn = null;
        PreparedStatement ps = null;

        try {

            final String path = "G:\\图片\\红.jpg";
            photo photo = new photo(4, "zhazha", readImage(path));

            conn = getConnection();
            //执行SQL语句
            ps = conn.prepareStatement("insert into jdbc.photo (id,name,photo)values(?,?,?)");
            //设置参数
            ps.setInt(1, photo.getId());
            ps.setString(2, photo.getName());
            ps.setBinaryStream(3, photo.getIn(), photo.getIn().available());   //设置二进制流

            System.out.println((ps.executeUpdate()) > 0 ? "插入成功！" : "插入失败");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(conn, null, null);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void test1() {

        // 读取数据库中图片


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            final String targetPath = "G:\\图片\\20200918185440.jpg";
            photo photo = new photo(2);

            conn = getConnection();
            //执行sql语句
            ps = conn.prepareStatement("select * from jdbc.photo where id =?");
            ps.setInt(1, photo.getId());
            rs = ps.executeQuery();

            while (rs.next()) {
                photo.setIn(rs.getBinaryStream("photo"));
                readBinImage(photo.getIn(), targetPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(conn, null, null);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
