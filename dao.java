package photo;


import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * BaseDAO
 * wyl 2021-5
 **/
@SuppressWarnings("all")
public abstract class DAO {

    /**
     * 配置文件设置
     */
    private static final Properties properties = new Properties();
    private static final String propertiesPath = "G:\\idea\\JDBC\\properties\\druid.properties";

    /**
     * 配置文件加载
     */
    static {
        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    连接
     */
    public Connection getConnection() throws Exception {
        return DruidDataSourceFactory.createDataSource(properties).getConnection();
    }

    /*
    断开连接
     */
    public void closeResourse(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    

}
