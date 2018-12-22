package com.it666.jdbc.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryClass {
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://localhost:3306/jdbc_db";
		String user = "root";
		String pwd = "227492259";

		// 1.��������
		Class.forName("com.mysql.jdbc.Driver");
		// 2.�������ݿ�
		Connection conn = DriverManager.getConnection(url, user, pwd);
		// 3.��дSQL����
		String sql = "select * FROM emp";
		Statement st = conn.createStatement();
		// 4.ִ��SQL����
		ResultSet res = st.executeQuery(sql);
		while (res.next()) {
			int empno = res.getInt("empno");
			String ename = res.getString("ename");
			String job = res.getString("job");
			System.out.println("empno=" + empno + "  ename=" + ename + "      job=" + job);
		}

		// 5.�ͷ���Դ
		st.close();
		conn.close();

	}

	// ȡһ������
	void Test1() throws Exception {
		String url = "jdbc:mysql://localhost:3306/jdbc_db";
		String user = "root";
		String pwd = "227492259";

		// 1.��������
		Class.forName("com.mysql.jdbc.Driver");
		// 2.�������ݿ�
		Connection conn = DriverManager.getConnection(url, user, pwd);
		// 3.��дSQL����
		String sql = "select * FROM emp WHERE ename='³��';";
		Statement st = conn.createStatement();
		// 4.ִ��SQL����
		ResultSet res = st.executeQuery(sql);
		if (res.next()) {
			int empno = res.getInt("empno");
			String ename = res.getString("ename");
			String job = res.getString("job");
			System.out.println("empno=" + empno + "  ename=" + ename + "  job=" + job);
		}

		// 5.�ͷ���Դ
		st.close();
		conn.close();
	}

	// ȡһ������
	void Test() throws Exception {
		String url = "jdbc:mysql://localhost:3306/jdbc_db";
		String user = "root";
		String pwd = "227492259";

		// 1.��������
		Class.forName("com.mysql.jdbc.Driver");
		// 2.�������ݿ�
		Connection conn = DriverManager.getConnection(url, user, pwd);
		// 3.��дSQL����
		String sql = "select count(*) as total from stu";
		Statement st = conn.createStatement();
		// 4.ִ��SQL����
		ResultSet res = st.executeQuery(sql);
		if (res.next()) {
			int total = res.getInt("total");
			System.out.println(total);
		}

		// 5.�ͷ���Դ
		st.close();
		conn.close();
	}
}
