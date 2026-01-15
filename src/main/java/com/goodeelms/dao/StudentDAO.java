package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.DBUtil;

public class StudentDAO {
	public List<StudentMajorDTO> getMajors(String studentId) {
		String sql = "SELECT * FROM student_major WHERE student_id = ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, studentId);
			try(ResultSet rs = pstmt.executeQuery()){
				List<StudentMajorDTO> list = new ArrayList<StudentMajorDTO>();
				
				while(rs.next()) {
					StudentMajorDTO dto = new StudentMajorDTO();
					dto.setStudentId(Integer.parseInt(rs.getString("student_id")));
					dto.setMajorId(Integer.parseInt(rs.getString("major_id")));
					list.add(dto);
				}
				
				return list;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getStudentId(String studentNo) {
		String sql = "SELECT student_id FROM student WHERE student_no = ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, studentNo);
			try(ResultSet rs = pstmt.executeQuery()){
				if(!rs.next()) return null;
				return rs.getString("student_id");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
