package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.DBUtil;
import com.goodeelms.dto.StudentDTO;

public class StudentDAO {
private static final StudentDAO instance = new StudentDAO();
	
	private StudentDAO() {
	}
	
	public static StudentDAO getInstance() {
		return instance;
	}
	
	public StudentDTO checkStudent(String student_no, String student_password) {
		String sql = "SELECT * FROM student WHERE " + " student_no = ? AND student_password = ? ";

		StudentDTO dto = new StudentDTO();

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, student_no);
			pstmt.setString(2, student_password);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs == null) {
					System.out.println("student 테이블에 일치하는 데이터가 없습니다.");
					return null;
				}

				if (rs.next()) {
					dto.setStudentId(rs.getInt("student_id"));
					dto.setStudentNo(rs.getString("student_no"));
					dto.setStudentName(rs.getString("student_name"));
				}
			}
		} catch (Exception e) {
			System.out.println("searchStudent() 메서드 예외 발생: " + e.getMessage());
		}
		return dto;
	}

	public int updateStudent(StudentDTO dto , String orgin_student_password) {
		
		String sql = "UPDATE student SET student_password = ? , student_name = ? , "
				+ " student_phone = ? , student_identity_number = ? , student_gender = ? , "
				+ " student_address = ?, student_email = ? , student_bank = ? "
				+ "	WHERE student_no = ?";
		
		int result = 0;
		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, dto.getStudentPassword());
			pstmt.setString(2, dto.getStudentName());
			pstmt.setString(3, dto.getStudentPhone());
			pstmt.setString(4, dto.getStudentIdentityNumber());
			pstmt.setString(5, dto.getStudentGender());
			pstmt.setString(6, dto.getStudentAddress());
			pstmt.setString(7, dto.getStudentEmail());
			pstmt.setString(8, dto.getStudentBank());
			pstmt.setString(9, orgin_student_password);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("addStudent() 메서드 예외 발생: " + e.getMessage());
		}
		return result;
	}

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
