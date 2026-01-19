package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.util.DBUtil;

public class StudentRegisterDAO {
	
	private static final StudentRegisterDAO instance = new StudentRegisterDAO();
	
	private StudentRegisterDAO() {
	}
	
	public static StudentRegisterDAO getInstance() {
		return instance;
	}
	
	// 전체 학생 목록 조회 (id 내림차순으로 해야 방금 막 등록한 학생을 확인할 수 있음)
	public ArrayList<StudentDTO> getAllStudentList() {
		String sql = "SELECT s.student_id, student_no, student_name, student_identity_number, " +
				     "GROUP_CONCAT(DISTINCT m.major_name ORDER BY m.major_name SEPARATOR ', ') as major_name, " +
				     "student_phone, student_gender, student_address, student_status, student_bank " +
					 "FROM student s JOIN student_major sm ON s.student_id = sm.student_id " +
				     "JOIN major m ON sm.major_id = m.major_id " +
				     "GROUP BY s.student_id " +
					 "ORDER BY s.student_id DESC ";
	
		
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
	
	// 학생 등록란에 사용할 학과 목록 조회
	public ArrayList<MajorDTO> getMajorList() {
		String sql = "SELECT major_id, major_name FROM major ORDER BY major_name";
		
		ArrayList<MajorDTO> majorList = new ArrayList<MajorDTO>();
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					MajorDTO majorDTO = new MajorDTO();
					majorDTO.setMajorId(rs.getInt("major_id"));
					majorDTO.setMajorName(rs.getString("major_name"));
					majorList.add(majorDTO);
				}
			}
		} catch (Exception e) {
			System.out.println("getMajorList() 예외 발생: " + e);
		} return majorList;
	}
	
	// 사용중인 학번인지 확인
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
	
	// 새 학생 등록
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
	
	// 검색 조건에 따른 학생 목록 조회
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

	
	// student_id의 max값을 조회해서 방금 등록한 학생의 id를 return
	public int getNewStudentId() {
		String sql = "SELECT MAX(student_id) as new_id FROM student";
		
		int newStudentId = 0;
		try(Connection conn = DBUtil.getConnection();
		    PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					newStudentId = rs.getInt("new_id");
				}
			}
		} catch (Exception e) {
			System.out.println("getNewStudentId() 예외 발생: " + e);
		} return newStudentId;
	}
	
	// student_major 테이블 작성
	public int writeStudentMajor(int newStudentId, int majorId) {
		String sql = "INSERT INTO student_major (student_id, major_id) VALUES (?, ?) ";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, newStudentId);
			pstmt.setInt(2, majorId);
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("writeStudentMajor() 예외 발생: " + e);
			return 0;
		}
	}
	
	
}

