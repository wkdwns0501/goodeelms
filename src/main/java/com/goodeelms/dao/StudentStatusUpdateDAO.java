package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import com.goodeelms.dto.StudentDTO;
import com.goodeelms.util.DBUtil;

public class StudentStatusUpdateDAO {
	
	private static final StudentStatusUpdateDAO instance = new StudentStatusUpdateDAO();
	
	private StudentStatusUpdateDAO() {
	}
	
	public static StudentStatusUpdateDAO getInstance() {
		return instance;
	}
	
	// 화면 진입시 검색 조건 없는 전체 목록 불러오는 메서드
		public ArrayList<StudentDTO> getAllStudentList() {
			String sql = "SELECT s.student_id, s.student_no, s.student_name, "
		               + "GROUP_CONCAT(m.major_name ORDER BY m.major_name SEPARATOR ', ') as major_name, "
		               + "s.student_status "
		               + "FROM student s "
		               + "JOIN student_major sm ON s.student_id = sm.student_id " 
		               + "JOIN major m ON sm.major_id = m.major_id " 
		               + "GROUP BY s.student_id "
		               + "ORDER BY major_name, student_name"; 
			
			ArrayList<StudentDTO> list = new ArrayList<StudentDTO>();
			
			try(Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				try(ResultSet rs = pstmt.executeQuery()) {
					while(rs.next()) {
					StudentDTO studentDTO = new StudentDTO();
					studentDTO.setStudentId(rs.getInt("student_id"));
					studentDTO.setStudentNo(rs.getString("student_no"));
					studentDTO.setStudentName(rs.getString("student_name"));
					studentDTO.setMajorName(rs.getString("major_name"));
					studentDTO.setStudentStatus(rs.getString("student_status"));
					list.add(studentDTO);
					}
				}
			} catch (Exception e) {
				System.out.println("getAllStudentList() 예외 발생: " + e);
			} return list;
		}
		
		
	// 검색 조건으로 학생 list를 조회하는 메서드
	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		String sql = "SELECT s.student_id, student_no, student_name, GROUP_CONCAT(m.major_name ORDER BY m.major_name SEPARATOR ', ') as major_name, student_status "+
					 "FROM student s JOIN student_major sm ON s.student_id = sm.student_id " +
					 "JOIN major m ON sm.major_id = m.major_id " +
					 "WHERE 1=1 ";
		
		boolean isName = false;
		boolean isMajor = false;
		boolean isNo = false;
		
		// 검색 조건에 사용자의 기입 여부에 따라 쿼리문에 AND절 추가
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
		
			sql += "GROUP BY s.student_id ORDER BY major_name, student_name ";
		
		ArrayList<StudentDTO> list = new ArrayList<StudentDTO>();
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {	 
			
			// 쿼리문 ? 에 인덱스를 맞춰 파라미터 대입
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
			
			// id를 가져오는 이유 : UI에는 출력하지 않지만 상태 변경 저장을 했을때 해당 학생 id를 hidden으로 update 서비스로 보냄
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					StudentDTO studentDTO = new StudentDTO();
					studentDTO.setStudentId(rs.getInt("student_id"));;
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
	
	// student_status를 수정하는 메서드
	public int updateStudentStatus(String studentId, String newStudentStatus) {
		String sql = "UPDATE student SET student_status = ? WHERE student_id = ?";
		
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {	
			pstmt.setString(1, newStudentStatus);
			pstmt.setInt(2, Integer.parseInt(studentId));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateStudentStatus() 예외 발생: " + e);
			return 0;
		} 
	}
	
	// student_status_history 에 기록을 작성하는 메서드
	public int writeStatusHistory(String studentId, String newStudentStatus, String statusReason, String adminId) {
		String sql = "INSERT INTO student_status_history (status_type, status_reason, student_id, admin_id) "
					+ "VALUES (?, ?, ?, ?)";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {	
			pstmt.setString(1, newStudentStatus);
			pstmt.setString(2, statusReason);
			pstmt.setInt(3, Integer.parseInt(studentId));
			pstmt.setInt(4, Integer.parseInt(adminId));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("writeStatusHistory() 예외 발생: " + e);
			return 0;
		}
	}
	
	

}
