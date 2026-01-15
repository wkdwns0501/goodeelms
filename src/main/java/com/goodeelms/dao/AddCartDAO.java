package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.goodeelms.util.DBUtil;

public class AddCartDAO {
	private static final AddCartDAO instance = new AddCartDAO();

	public static AddCartDAO getInstance() {
		return instance;
	}
	
	public int insertLectureOnCart(String lectureId, String studentId) {
		String sql = "INSERT INTO pre_enrollment(lecture_id, student_id) VALUES(?, ?)";
		
		int index = 1;
		try(PreparedStatement pstmt = getPrepare(sql)){
			pstmt.setString(index++, lectureId);
			pstmt.setString(index++, studentId);
			
			int result = pstmt.executeUpdate();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int simpleSearchCart(String lectureId, String studentId) {
		String sql = "SELECT COUNT(*) as cnt FROM pre_enrollment WHERE lecture_id = ? AND student_id = ?";
		
		int index = 1;
		try(PreparedStatement pstmt = getPrepare(sql)){
			pstmt.setString(index++, lectureId);
			pstmt.setString(index++, studentId);
			
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {;
					return Integer.parseInt(rs.getString("cnt"));
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	private PreparedStatement getPrepare(String sql) throws SQLException {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		return pstmt;
	}
}
