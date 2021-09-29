

import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
//        Properties properties = new Properties();
//        properties.load(new FileInputStream("G:\\idea\\JDBC\\properties\\mysql.properties"));                 //获取连接信息
//        Class.forName(properties.getProperty("driver"));
//        return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));

//        Properties properties = new Properties();
//        properties.load(new FileInputStream("G:\\idea\\JDBC\\properties\\druid.properties"));

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

    public void closeResourse(Connection connection, Statement statement) {
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

    public void closeResourse(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /*
    更新成功 返回1 失败返回0
     */
    @Deprecated
    public int updateStatement(Connection connection, String sql) {
        Statement statement = null;
        try {
            //得到语句对象
            statement = connection.createStatement();
            //执行sql语句
//            boolean execute = statement.execute(sql);//返回是否有结果集
            return statement.executeUpdate(sql);    //影响行数
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //断开连接
            closeResourse(null, statement);
        }
        return 0;
    }

    /*
    更新成功 返回1 失败返回0
     */
    public int UpdatePreparedStatement(Connection connection, String sql, Object... args) {   //sql中占位符的个数于可变性惨的长度相同
        PreparedStatement preparedStatement = null;
        try {
            //预编译
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行操作
            //preparedStatement.execute();    执行语句<查询>有返回结果为true   <增删改>没有有返回结果false
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //断开连接
            closeResourse(null, preparedStatement);
        }
        return 0;
    }

    /*
    查询一个值
     */
    @Deprecated
    public <T> T getInstanceStatement(Connection connection, Class<T> clazz, String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //得到语句对象
            statement = connection.createStatement();
            //执行语句获取结果集
            resultSet = statement.executeQuery(sql);

            ResultSetMetaData metaData = resultSet.getMetaData();    //获取结果集元数据

            int columnCount = metaData.getColumnCount();     //得到结果集列数
            if (resultSet.next()) {
                T t = clazz.newInstance();      //创建对象
                //处理数据
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);   //获取列值
                    String columnLabel = metaData.getColumnLabel(i + 1);          //获取列别名          有别名用别名  没有则使用列名
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, statement, resultSet);
        }
        return null;
    }

    /*
    查询多个值
     */
    @Deprecated
    public <T> List<T> getForListStatement(Connection connection, Class<T> clazz, String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();   //得到语句对象
            resultSet = statement.executeQuery(sql);    //执行语句获取结果集

            ResultSetMetaData metaData = resultSet.getMetaData();    //获取结果集元数据

            int columnCount = metaData.getColumnCount(); //得到结果集列数
            ArrayList<T> list = new ArrayList<T>();    //创建集合
            while (resultSet.next()) {
                T t = clazz.newInstance();
                //处理数据  给每一个t对象肤质，并且加入集合中
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);  //获取列值
                    String columnLabel = metaData.getColumnLabel(i + 1);         //获取列别名    有别名用别名  没有则使用列名
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, statement, resultSet);
        }
        return null;
    }

    /*
    查询一个值  查到返回该对象
     */
    public <T> T getInstancePreparedStatement(Connection connection, Class<T> clazz, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            preparedStatement = connection.prepareStatement(sql);//预编译sql语句
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            //执行语句获取结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //得到结果集列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();
                //处理数据
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列别名      有别名用别名  没有则使用列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, preparedStatement, resultSet);
        }
        return null;
    }

    /*
    查询多个值
     */
    public <T> List<T> getForListPreparedStatement(Connection connection, Class<T> clazz, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行语句获取结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //得到结果集列数
            int columnCount = metaData.getColumnCount();
            //创建集合
            ArrayList<T> list = new ArrayList<T>();
            while (resultSet.next()) {
                T t = clazz.newInstance();
                //处理数据  给每一个t对象肤质，并且加入集合中
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列别名    有别名用别名  没有则使用列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, preparedStatement, resultSet);
        }
        return null;
    }

    //查询特殊值
    public <E> E getValue(Connection connection, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResourse(null, preparedStatement, resultSet);
        }
        return null;
    }

}
