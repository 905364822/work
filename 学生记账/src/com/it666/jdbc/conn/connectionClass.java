package com.it666.jdbc.conn;

import java.sql.Connection;
import java.sql.DriverManager;

public class connectionClass {
	public static void main(String[] args) throws Exception {
		//1.��������
		//��com.mysql.jdbc.Driver����ֽ�����ؽ�JVM
		//��һ���ֽ��뱻���ص�JVMʱ���ͻ�ִ�и��ֽ����еľ�̬�����
		Class.forName("com.mysql.jdbc.Driver");//javaWeb��������������仰��Ҫд����
		
		//2.��ȡ���ӵĶ���
		//url
		String url = "jdbc:mysql://localhost:3306/my_test2";
		//user �û���
		String user = "root";
		//password ����
		String password = "227492259";
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
		Thread.sleep(50000);
	}
}
