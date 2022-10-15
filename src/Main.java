import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException{
        Connection conn=JDBCUtils.getConnection();
        Statement statement=conn.createStatement();
        Main test=new Main();
        test.init();
        test.CRUD();
        test.select();
    }
    //初始化数据库
    public void init() throws SQLException{
        Connection conn=JDBCUtils.getConnection();
        Statement statement=conn.createStatement();
        statement.executeUpdate("create database if not exists test");
        statement.executeUpdate("use test");
        //用户表
        String sql1="create table if not exists `Users`(" +
                "`id` int(8) not null auto_increment comment '用户id'," +
                "`username` varchar(30) not null comment '用户名'," +
                "`email` varchar(30) default null comment '邮箱'," +
                "`password` varchar(20) default null comment '密码', " +
                "primary key (`id`)" +
                ")engine=InnoDB default charset=utf8;";
        statement.executeUpdate(sql1);
        //食物表
        String sql2="create table if not exists `Food`(" +
                "`id` int(8) not null auto_increment comment '食物id'," +
                "`name` varchar(30) not null comment '食物名称'," +
                "`canteen` varchar(30) default null comment '食堂'," +
                "`floor` varchar(30) default null comment '楼层'," +
                "primary key (`id`)" +
                ")engine=InnoDB default charset=utf8;";
        statement.executeUpdate(sql2);
        //收藏表
        String sql3="create table if not exists `Collection`(" +
                "`food_id` int(8) default null comment '食物id'," +
                "`user_id` int(8) not null auto_increment comment '用户id'," +
                "key `user_id` (`user_id`)" +
                ")engine=InnoDB default charset=utf8;";
        statement.executeUpdate(sql3);

        //释放连接
        JDBCUtils.release(conn,statement,null);
    }

    //CRUD
    public void CRUD() throws SQLException{
        Connection conn=JDBCUtils.getConnection();
        Statement statement=conn.createStatement();
        statement.executeUpdate("use test");

        //用户表
        //insert into Users (...) values (...);
        String u_sql1="insert into Users (id,username,email,password) values (1001,'张三','123.@cumt.com','12345');";
        statement.executeUpdate(u_sql1);
        //delete from Users where ... ; truncate 清空
        String u_sql2="delete from Users where id=1001;";
        //update Users set ... where ... ;
        String u_sql3="update Users set user_name='李四' where id=1001;";
        //select ... from Users where ... ;
        String u_sql4="select * from Users;";
        ResultSet resultSet=statement.executeQuery(u_sql4);
        while(resultSet.next()){
            System.out.println("id: "+resultSet.getObject("id"));
            System.out.println("username: "+resultSet.getObject("username"));
            System.out.println("email: "+resultSet.getObject("email"));
            System.out.println("password: "+resultSet.getObject("password"));
        }

        //食物表（与用户表类似）
        statement.executeUpdate("insert into Food values(2034,'可乐','三食堂','二楼');");
        //收藏表
        statement.executeUpdate("insert into Collection select f.id,u.id from Food f,Users u;");

        //释放连接
        JDBCUtils.release(conn,statement,resultSet);
    }

    //随机食物及筛选
    public void select() throws SQLException{
        Connection conn=JDBCUtils.getConnection();
        Statement statement=conn.createStatement();
        statement.executeUpdate("use test");

        //随机
        statement.executeUpdate("insert into Food values (1234,'炸鸡','三食堂','一楼'),(1002,'麻辣烫','二食堂','三楼');");
        String sql="select * from Food where id=(select floor(rand()*max(id)) from Food);";
        ResultSet resultSet=statement.executeQuery(sql);
        while(resultSet.next()){
            System.out.println("id: "+resultSet.getObject("id"));
            System.out.println("name: "+resultSet.getObject("name"));
            System.out.println("canteen: "+resultSet.getObject("canteen"));
            System.out.println("floor: "+resultSet.getObject("floor"));
        }
        //筛选
        resultSet=statement.executeQuery("select * from Food where canteen='三食堂' && floor='一楼';");
        while(resultSet.next()){
            System.out.println("id: "+resultSet.getObject("id"));
            System.out.println("name: "+resultSet.getObject("name"));
            System.out.println("canteen: "+resultSet.getObject("canteen"));
            System.out.println("floor: "+resultSet.getObject("floor"));
        }

        //释放连接
        JDBCUtils.release(conn,statement,resultSet);
    }
}