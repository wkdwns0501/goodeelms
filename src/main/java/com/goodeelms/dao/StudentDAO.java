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
		String sql = "SELECT * FROM student WHERE student_no = ? AND student_password = ? ";

		StudentDTO dto = null;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, student_no);
			pstmt.setString(2, student_password);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs == null) {
					return null;
				}

				if (rs.next()) {
					dto = new StudentDTO();	// 세션에 추가할 속성이 있다면, dto를 이용
					
					dto.setStudentId(rs.getInt("student_id"));
					dto.setStudentNo(rs.getString("student_no"));
					dto.setStudentName(rs.getString("student_name"));
					dto.setStudentPassword(rs.getString("student_password"));  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public int updateStudent(StudentDTO dto, int student_id) throws SQLException {
	    String sql = "UPDATE student SET student_password = ?, student_phone = ?, "
	               + "student_address = ?, "
	               + "student_email = ?, student_bank = ? "
	               + "WHERE student_id = ?";
	        
	    int result = 0;
	    
	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, dto.getStudentPassword());
	        pstmt.setString(2, dto.getStudentPhone());
	        pstmt.setString(3, dto.getStudentAddress());
	        pstmt.setString(4, dto.getStudentEmail());
	        pstmt.setString(5, dto.getStudentBank());
	        pstmt.setInt(6, student_id);
	        
	        result = pstmt.executeUpdate();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}

	public boolean isPhoneAndEmailExist(String phone, String email) {
		String sql = "SELECT COUNT(*) as cnt FROM student "
				+ "WHERE student_phone = ? OR student_email = ?";
		
		boolean result = false;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, phone);
			pstmt.setString(2, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("cnt");
					if (count > 0) {
						result = true;	// 이미 사용하느 전화번호, 주민번호, 이메일이 있을 경우 
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
