package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.DBUtil;

public class StudentStatusUpdateDAO {
	
	private static final StudentStatusUpdateDAO instance = new StudentStatusUpdateDAO();
	
	private StudentStatusUpdateDAO() {
	}
	
	public static StudentStatusUpdateDAO getInstance() {
		return instance;
	}
	
	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		String sql = "SELECT student_no, student_name, GROUP_CONCAT(m.major_name ORDER BY m.major_name SEPARATOR ', ') as major_name, student_status "+
					 "FROM student s JOIN student_major sm ON s.student_id = sm.student_id " +
					 "JOIN major m ON sm.major_id = m.major_id " +
					 "WHERE 1=1 ";
		
		boolean isName = false;
		boolean isMajor = false;
		boolean isNo = false;
		
		if(studentName != null && !studentName.trim().isEmpty()) {
			sql += "AND s.student_name LIKE ? ";
			isName = true;
		} 
		if (majorName != null && !majorName.trim().isEmpty()) {
			sql += "AND m.major_name LIKE ? ";
			isMajor = true;
		} 
		if (studentNo != null && !studentNo.trim().isEmpty()) {
			sql += "AND s.student_no = LIKE ? ";
			isNo = true;
		}
		
			sql += "GROUP BY s.student_id ORDER BY major_name, student_name ";
		
		ArrayList<StudentDTO> list = new ArrayList<StudentDTO>();
		
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {	 
			
			int index = 1;
			if(isName) {
				pstmt.setString(index++, "%" + studentName + "%");
			}
			if(isMajor) {
				pstmt.setString(index++, "%" + majorName + "%");
			}
			if(isNo) {
				pstmt.setString(index++, "%" + studentNo + "%");
			}
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					StudentDTO studentDTO = new StudentDTO();
					studentDTO.setStudentNo(rs.getString("student_no"));
					studentDTO.setStudentName(rs.getString("student_name"));
					studentDTO.setMajorName(rs.getString("major_name"));
					studentDTO.setStudentStatus(rs.getString("student_status"));
					list.add(studentDTO);
				}
			}
		} catch (Exception e) {
			System.out.println("getStudentList() 예외 발생: " + e);
		} return list;
	}
}
