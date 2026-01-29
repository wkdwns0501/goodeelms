package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.LectureHistoryDTO;
import com.goodeelms.dto.ScholarshipDTO;
import com.goodeelms.util.DBUtil;

public class ScholarshipDAO {
	
	private static final ScholarshipDAO instance = new ScholarshipDAO();
	
	private ScholarshipDAO() {
	}
	
	public static ScholarshipDAO getInstance() {
		return instance;
	}

	public ArrayList<LectureHistoryDTO> getScholarshipList(String year, int semester) {
		String sql = "SELECT t.*, " +
	                 "CASE WHEN sh.student_id IS NOT NULL THEN 'Y' ELSE 'N' END AS is_confirmed " +
	                 "FROM (SELECT s.student_id, s.student_no, s.student_name, m.major_name, ROUND(AVG(lh.lecture_score), 2) AS avg_score, " +
	                 "ROW_NUMBER() OVER (PARTITION BY m.major_id ORDER BY ROUND(AVG(lh.lecture_score),2) DESC) AS rn " +
	                 "FROM lecture_history lh JOIN lecture l ON lh.lecture_id = l.lecture_id " +
	                 "JOIN student s ON s.student_id = lh.student_id " +
	                 "JOIN student_major sm ON sm.student_id = s.student_id " +
	                 "JOIN major m ON m.major_id = sm.major_id " +
	                 "WHERE l.lecture_year = ? AND l.lecture_semester = ? " +
	                 "GROUP BY s.student_id, s.student_no, s.student_name, m.major_id, m.major_name) t " +
	                 // 성적 우수자 명단(t)에 실제 장학금 받은 이력(sh)을 왼쪽 결합
	                 "LEFT JOIN scholarship_history sh ON t.student_id = sh.student_id AND sh.scholarship_semester = ? " +
	                 "WHERE t.rn <= 3 " +
	                 "ORDER BY t.major_name, t.avg_score DESC";
		
		int yearSemester = Integer.parseInt(year + semester);
		ArrayList<LectureHistoryDTO> list = new ArrayList<LectureHistoryDTO>();
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
				pstmt.setString(1, year);
				pstmt.setInt(2, semester);
				pstmt.setInt(3, yearSemester);
				System.out.println(yearSemester);
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					LectureHistoryDTO lectureHistoryDTO = new LectureHistoryDTO();
					lectureHistoryDTO.setStudentId(rs.getInt("student_id"));
					lectureHistoryDTO.setStudentNo(rs.getInt("student_no"));
					lectureHistoryDTO.setStudentName(rs.getString("student_name"));
					lectureHistoryDTO.setMajorName(rs.getString("major_name"));
					lectureHistoryDTO.setGpa(rs.getDouble("avg_score"));
					lectureHistoryDTO.setIsConfirmed(rs.getString("is_confirmed"));
					list.add(lectureHistoryDTO);				
				}
			}
		} 	catch (Exception e) {
			System.out.println("getScholarshipList() 예외 발생: " + e);
		}	return list;
	}

	public int writeScholarshipHistory(String[] confirmedStudentsId, int yearSemestertoInt) {
		String sql = "INSERT INTO scholarship_history (scholarship_semester, student_id) " +
					 "VALUES(?, ?)";
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// 자동 커밋 off -> 모두 성공해야 성공 -> 데이터 꼬임 방지
	        conn.setAutoCommit(false);

	        for (String id : confirmedStudentsId) {
	            pstmt.setInt(1, yearSemestertoInt);
	            pstmt.setInt(2, Integer.parseInt(id));
	            pstmt.addBatch(); // 실행 대기열에 추가
	        }
	        
	        // 대기열에 있는 쿼리문 모두 실행
	        int[] resultCount = pstmt.executeBatch(); 
	        conn.commit(); // 커밋 실행

	        return resultCount.length; // 성공한 행의 개수 반환
		} catch (SQLException e) {
			System.out.println("writeScholarshipHistory() 예외 발생: " + e);
			return 0;
		}
	}
	
	
	public List<ScholarshipDTO> getSemesterAndAmountByStudentId(int studentId){
		String sql = "SELECT SH.scholarship_semester, SH.scholarship_amount "
				+ "FROM scholarship_history SH "
				+ "JOIN student S ON S.student_id = SH.student_id "
				+ "WHERE sh.student_id = ?  "
				+ "ORDER BY SH.scholarship_semester DESC ";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			
			try (ResultSet rs = pstmt.executeQuery()) {  
				List<ScholarshipDTO> list = new ArrayList<ScholarshipDTO>();
				
				while (rs.next()) {
					ScholarshipDTO dto = new ScholarshipDTO();
					dto.setScholarshipSemester(rs.getInt("scholarship_semester"));
					dto.setScholarshipAmount(rs.getInt("scholarship_amount"));
					list.add(dto);
				}
				return list;
			} catch (Exception e) {
				System.out.println("getSemesterAndAmountByStudentId() 쿼리 실행 예외: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getSemesterAndAmountByStudentId() DB 연결 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public int cancelScholarship(String studentId, int yearSemesterInt) {
		String sql = "DELETE FROM scholarship_history WHERE student_id = ? AND scholarship_semester = ? ";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, Integer.parseInt(studentId));
			pstmt.setInt(2, yearSemesterInt);
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("cancelScholarship() 예외 발생: " + e);
		}	return 0;
	}
	
	
}
