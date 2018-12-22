package com.it666.jdbc.conn;

import java.sql.Connection;
import java.sql.DriverManager;

public class connectionClass {
	public static void main(String[] args) throws Exception {
		//1.加载驱动
		//把com.mysql.jdbc.Driver这份字节码加载进JVM
		//当一份字节码被加载到JVM时，就会执行该字节码中的静态代码块
		Class.forName("com.mysql.jdbc.Driver");//javaWeb不会主动生成这句话，要写！！
		
		//2.获取连接的对象
		//url
		String url = "jdbc:mysql://localhost:3306/my_test2";
		//user 用户名
		String user = "root";
		//password 密码
		String password = "227492259";
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
		Thread.sleep(50000);
	}
}
