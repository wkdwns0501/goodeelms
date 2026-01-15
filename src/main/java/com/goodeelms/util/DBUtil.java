package com.goodeelms.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/goodeelmsdb";
	private static final String USER = "root";
	private static final String PASSWORD = "roqkf@12";
	
	// 커넥션 생성 메소드
	public static Connection getConnection() {
		try{
			// JDBC 드라이버 로딩
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Conncection 객체 얻기(JDBC 드라이버 -> DB 연결)
			return DriverManager.getConnection(URL	, USER, PASSWORD);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	// 리소스 해제 메소드
	public static void DBConnectionsClose(AutoCloseable ...closeable) {
		for(AutoCloseable closer : closeable) {
			if(closer != null) {
				try {
					closer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
