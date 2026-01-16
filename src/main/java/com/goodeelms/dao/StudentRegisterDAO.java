package com.goodeelms.dao;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.util.DBUtil;

public class StudentRegisterDAO {
	
	private static final StudentRegisterDAO instance = new StudentRegisterDAO();
	
	private StudentRegisterDAO() {
	}
	
	public static StudentRegisterDAO getInstance() {
		return instance;
	}

	public int studentExistCheck(String studentNo) {
		String sql = "SELECT COUNT(*) FROM student WHERE student_no = ? ";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, Integer.parseInt(studentNo));
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) return rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("studentExistCheck() 예외 발생: " + e);
		}	return 0 ;
	}
	
	public int studentRegister(StudentDTO studentDTO) {
		String sql = "INSERT INTO student (student_name, student_password, student_phone, student_gender, student_identity_number, student_no) "
					+"VALUES (?, ?, ?, ?, ?, ?)";
			
			String identityNumber = studentDTO.getStudentIdentityNumber();
			String password = identityNumber.substring(identityNumber.length() - 7);
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, studentDTO.getStudentName());
			pstmt.setString(2, password);
			pstmt.setString(3, studentDTO.getStudentPhone());
			pstmt.setString(4, studentDTO.getStudentGender());
			pstmt.setString(5, studentDTO.getStudentIdentityNumber());
			pstmt.setString(6, studentDTO.getStudentNo());
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("studentRegister() 예외 발생: " + e);
		}	return 0;
	}

	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		String sql = "SELECT s.student_no, s.student_name, s.student_identity_number, " +
					 "GROUP_CONCAT(DISTINCT m.major_name ORDER BY m.major_name SEPARATOR ', ') as major_name, " +
					 "s.student_phone, s.student_gender, s.student_address, s.student_status, s.student_bank " +
					 "FROM student s " +
					 "LEFT JOIN student_major sm ON s.student_id = sm.student_id " + // 전공이 없을 수도 있으므로 LEFT JOIN 권장
					 "LEFT JOIN major m ON sm.major_id = m.major_id " +
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
			sql += "AND s.student_no LIKE ? ";
			isNo = true;
		}
		
			sql += " GROUP BY s.student_id, s.student_no, s.student_name, s.student_identity_number, " +
			       " s.student_phone, s.student_gender, s.student_address, s.student_status, s.student_bank " +
			       " ORDER BY s.student_id DESC ";
		
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
					studentDTO.setStudentIdentityNumber(rs.getString("student_identity_number"));
					studentDTO.setMajorName(rs.getString("major_name"));
					studentDTO.setStudentPhone(rs.getString("student_phone"));
					studentDTO.setStudentGender(rs.getString("student_gender"));
					studentDTO.setStudentAddress(rs.getString("student_address"));
					studentDTO.setStudentStatus(rs.getString("student_status"));
					studentDTO.setStudentBank(rs.getString("student_bank"));
					list.add(studentDTO);
				}
			}
		} catch (Exception e) {
			System.out.println("getStudentList() 예외 발생: " + e);
		} return list;
	}

	public ArrayList<StudentDTO> getAllStudentList() {
		String sql = "SELECT *, GROUP_CONCAT(DISTINCT m.major_name ORDER BY m.major_name SEPARATOR ', ') as major_name, "
				   + " FROM student s JOIN"
				   + "ORDER BY student_id DESC " +
	
		
		ArrayList<StudentDTO> list = new ArrayList<StudentDTO>();
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					StudentDTO studentDTO = new StudentDTO();
					studentDTO.setStudentNo(rs.getString("student_no"));
					studentDTO.setStudentName(rs.getString("student_name"));
					studentDTO.setStudentIdentityNumber(rs.getString("student_identity_number"));
					studentDTO.setMajorName(rs.getString("major_name"));
					studentDTO.setStudentPhone(rs.getString("student_phone"));
					studentDTO.setStudentGender(rs.getString("student_gender"));
					studentDTO.setStudentAddress(rs.getString("student_address"));
					studentDTO.setStudentStatus(rs.getString("student_status"));
					studentDTO.setStudentBank(rs.getString("student_bank"));
					list.add(studentDTO);
				}
			}
			
			
		} catch (SQLException e) {
			System.out.println("getAllStudentList() 예외 발생: " + e);
		}	return list;
	}
}
