package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.util.DBUtil;
import com.mysql.cj.exceptions.RSAException;

public class StudentDAO {

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
					dto.setStudentNo(rs.getString("student_no"));
					dto.setStudentPassword(rs.getString("student_password"));
				}
			}
		} catch (Exception e) {
			System.out.println("searchStudent() 메서드 예외 발생: " + e.getMessage());
		}
		return dto;
	}

	public void addStudent(String login_student_no, String student_no, String student_password, String student_name, String student_phone, 
			String student_identity_number, String student_gender, String student_address, String student_email, String student_bank) {
		
		String sql = "UPDATE student SET student_no = ? , student_password = ? , student_name = ? , "
				+ " student_phone = ? , student_identity_number = ? , student_gender = ? , "
				+ " student_address = ?, student_email = ? , student_bank = ? "
				+ "	WHERE student_no = ?";

		StudentDTO dto = new StudentDTO();

		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, student_no);
			pstmt.setString(2, student_password);
			pstmt.setString(3, student_name);
			pstmt.setString(4, student_phone);
			pstmt.setString(5, student_identity_number);
			pstmt.setString(6, student_gender);
			pstmt.setString(7, student_address);
			pstmt.setString(8, student_email);
			pstmt.setString(9, student_bank);
			pstmt.setString(10, login_student_no);

			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("addStudent() 메서드 예외 발생: " + e.getMessage());
		}
	}
	
	public ResultSet selectStudent(String stustudent_no) {
		String sql = "SELECT * FROM student WHERE student_no = ?";
				
		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, student_no);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs == null) {
					System.out.println("student 테이블에 일치하는 데이터가 없습니다.");
					return null;
				}

				if (rs.next()) {
					dto.setStudentNo(rs.getString("student_no"));
					dto.setStudentPassword(rs.getString("student_password"));
				}
			}
		} catch (Exception e) {
			System.out.println("searchStudent() 메서드 예외 발생: " + e.getMessage());
		}
	}
	

}
