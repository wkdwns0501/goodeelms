package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.util.DBUtil;

public class LectureHistoryDAO {
	private static final LectureHistoryDAO instance = new LectureHistoryDAO();

	private LectureHistoryDAO() {
	}

	public static LectureHistoryDAO getInstance() {
		return instance;
	}
	
	public Map<Integer, LectureDTO> getLectureHistoryByStudentId(int studentId) { // 0120 임욱(추가) / 수강했던 전체 강의 이력 조회
		String sql = "SELECT L.lecture_id, L.lecture_code, L.lecture_name, "
				+ "L.lecture_year, L.lecture_semester, LH.lecture_score "
				+ "FROM lecture_history LH "
				+ "JOIN lecture L ON L.lecture_id = LH.lecture_id "
				+ "WHERE LH.student_id = ? ";

		Map<Integer, LectureDTO> map = new LinkedHashMap<>();
		
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			try (ResultSet rs = pstmt.executeQuery()) {

				while(rs.next()) {
					LectureDTO dto = new LectureDTO();
					int lectureId = rs.getInt("lecture_id");
					dto.setLectureId(lectureId);
	                dto.setLectureName(rs.getString("lecture_name"));
	                dto.setLectureCode(rs.getInt("lecture_code"));
	                dto.setLectureYear(rs.getString("lecture_year")); 
	                dto.setLectureSemester(rs.getInt("lecture_semester"));
	                dto.setScore(rs.getDouble("lecture_score"));
	                
	                map.put(lectureId, dto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public Map<Integer, LectureDTO> getProgressInfoByStudentId(int studentId, int year, int semester) { // 0125 임욱(수정) / 해당 학기에 수강중인 강의 정보 조회
	    String sql = "SELECT l.lecture_id, l.lecture_code, l.lecture_name, l.lecture_room, "
	            + "l.lecture_credit, l.lecture_section, m.major_name, "
	            + "l.lecture_type, p.professor_name, b.building_name, lh.lecture_score "
	            + "FROM lecture_history lh "
	            + "JOIN lecture l ON l.lecture_id = lh.lecture_id "
	            + "JOIN professor p ON l.professor_id = p.professor_id "
	            + "JOIN building b ON l.building_id = b.building_id "
	            + "JOIN major m ON m.major_id = l.major_id "
	            + "WHERE lh.student_id = ? AND l.lecture_year = ? AND l.lecture_semester = ?";

	    Map<Integer, LectureDTO> map = new LinkedHashMap<>();
	    
	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, studentId);
	        pstmt.setInt(2, year);
	        pstmt.setInt(3, semester);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while(rs.next()) {
	                LectureDTO dto = new LectureDTO();
	                int lectureId = rs.getInt("lecture_id");
	                dto.setLectureId(lectureId);
	                dto.setLectureName(rs.getString("lecture_name"));
	                dto.setLectureCode(rs.getInt("lecture_code"));
	                dto.setLectureRoom(rs.getString("lecture_room"));
	                dto.setLectureCredit(rs.getInt("lecture_credit"));
	                dto.setLectureSection(rs.getString("lecture_section"));
	                dto.setLectureType(rs.getString("lecture_type"));
	                dto.setProfessorName(rs.getString("professor_name"));
	                dto.setBuildingName(rs.getString("building_name"));
	                dto.setMajorName(rs.getString("major_name"));
	                
	                map.put(lectureId, dto);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("getProgressInfoByStudentId 예외발생: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return map;
	}
	
	public List<LectureDTO> getProbationByStudentId(int studentId){ // 0121 임욱(추가) / 평균점수 2.0 이하 학생 조회
		String sql = "SELECT lh.student_id, l.lecture_year, l.lecture_semester, ROUND(AVG(lh.lecture_score), 1) AS '평균점수' "
				+ "FROM lecture_history lh "
				+ "JOIN lecture l ON lh.lecture_id = l.lecture_id "
				+ "WHERE lh.student_id = ? "
				+ "GROUP BY l.lecture_year, l.lecture_semester, lh.student_id "
				+ "HAVING AVG(lh.lecture_score) <= 2.0 "
				+ "ORDER BY l.lecture_year DESC, l.lecture_semester DESC"; 
		
		List<LectureDTO> list = new ArrayList<LectureDTO>();
		
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					LectureDTO dto = new LectureDTO();
	                dto.setLectureYear(rs.getString("lecture_year"));
	                dto.setLectureSemester(rs.getInt("lecture_semester"));
	                dto.setScore(rs.getDouble("평균점수"));
	                
	                list.add(dto);
				}
			} catch (Exception e) {
				System.out.println("getProbationByStudentId 쿼리실행 중 예외발생: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getProbationByStudentId 예외발생: " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	
	// 수강신청 중복 회피
	public int searchLectureIdOfHistory(String student_id, String lecture_id) {
		String sql = "SELECT COUNT(*) as count FROM lecture_history WHERE student_id = ? AND lecture_id = ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setString(1, student_id);
			pstmt.setString(2, lecture_id);
			
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					int count = rs.getInt("count");
					return count;
				}
				return 0;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	// 수강신청 자동 승인 대상 History에 추가
	public int insertLectureHistoryByNewLecture(Connection conn, Set<Integer> lectureIdSet) {
		if(lectureIdSet.size() < 1) return 0;
		String placeholder = lectureIdSet.stream().map(id -> "?").collect(Collectors.joining(","));
		
		String sql = "INSERT INTO lecture_history (student_id, lecture_id) "
				+ "SELECT student_id, lecture_id "
				+ "FROM pre_enrollment "
				+ "WHERE lecture_id IN (" + placeholder + ") "
				+ "AND pre_enrollment_status = 'progress'";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			int index = 1;
			for(Integer id : lectureIdSet) {
				pstmt.setInt(index++, id);
			}
			
			int result = pstmt.executeUpdate();
			System.out.println(result);
			return result;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// 수강 강의 목록에 추가
	public int insertNewLectureToHistory(Connection conn, String student_id, String lecture_id) {
		String sql = "INSERT INTO lecture_history(student_id, lecture_id) "
				+ "VALUES(?, ?)";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setString(1, student_id);
			pstmt.setString(2, lecture_id);
			
			return pstmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}