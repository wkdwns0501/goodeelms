package com.goodeelms.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/goodeelmsdb";
	private static final String USER = "root";
	private static final String PASSWORD = "test1234";
	
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
