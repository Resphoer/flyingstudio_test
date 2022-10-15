import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    private static String driver=null;
    private static String username=null;
    private static String url=null;
    private static String password=null;
    static{
        try{
            InputStream inputStream=Main.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties=new Properties();
            properties.load(inputStream);
            driver=properties.getProperty("driver");
            url=properties.getProperty("url");
            username=properties.getProperty("username");
            password=properties.getProperty("password");
            Class.forName(driver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //获取连接
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,username,password);
    }
    //释放连接
    public static void release(Connection conn,Statement statement,ResultSet resultSet){
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
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
