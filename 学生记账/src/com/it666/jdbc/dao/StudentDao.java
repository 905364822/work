package com.it666.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentDao {
	public void save(Student stu) {
		String url = "jdbc:mysql://localhost:3306/jdbc_db";
		String user = "root";
		String pwd = "227492259";
		Connection conn = null;
		Statement st = null;

		try {
			//1.加载注册驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2.连接数据库
			conn = DriverManager.getConnection(url, user, pwd);
			
			
			//3.创建sql语句
			Integer id = stu.getId();
			String name = stu.getName();
			Integer age = stu.getAge();
			String sql = "insert into stu values ("+id+",'"+name+"',"+age+")";
			st = conn.createStatement();
			System.out.println(sql);
			
			//4.执行sql语句
			int row = st.executeUpdate(sql);
			System.out.println(row);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//5.释放资源
			if (st != null) {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {

				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
}
