package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.util.DBUtil;

public class LectureHistoryDAO {
	private static final LectureHistoryDAO instance = new LectureHistoryDAO();

	private LectureHistoryDAO() {
	}

	public static LectureHistoryDAO getInstance() {
		return instance;
	}

	public Map<Integer, LectureDTO> getLectureHistoryByStudentId(int studentId) {  
		String sql = "SELECT L.lecture_id, L.lecture_code, L.lecture_name, "
				+ "L.lecture_year, L.lecture_semester, LH.lecture_score "
				+ "FROM lecture_history LH "
				+ "JOIN lecture L ON L.lecture_id = LH.lecture_id "
				+ "WHERE LH.student_id = ?";

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
				System.out.println("getLectureHistoryByStudentId 쿼리실행 중 예외발생: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getLectureHistoryByStudentId 쿼리실행 중 예외발생: " + e.getMessage());
			e.printStackTrace();
		}
		return map;
	}

	
	
	
	
}
