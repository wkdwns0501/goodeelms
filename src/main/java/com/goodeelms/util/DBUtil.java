package com.goodeelms.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/goodeelmsdb";
	private static final String USER = "root";
<<<<<<< HEAD
	private static final String PASSWORD = "roqkf@12";
=======
	private static final String PASSWORD = "test4321";
>>>>>>> 89a1441a0cb82d0d1244dcbc1784bcd781704179

	// 커넥션 생성 메소드
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
	