package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureEvaluationDTO;
import com.goodeelms.util.DBUtil;

public class EvalutationDAO {
	
	private static final EvalutationDAO instance = new EvalutationDAO();
	
	private EvalutationDAO() {
	}
	
	public static EvalutationDAO getInstance() {
		return instance;
	}
	
	public ArrayList<LectureDTO> getLectureList(int studentId, String targetYear, int targetSemester) {
		String sql = "SELECT l.lecture_id, l.lecture_code, l.lecture_name, p.professor_name, " +
                	 "(SELECT COUNT(*) FROM lecture_evaluation le " +
                	 "WHERE le.lecture_id = l.lecture_id AND le.student_id = ?) as is_evaluated " +
                	 "FROM lecture_history lh " +
                	 "JOIN lecture l ON lh.lecture_id = l.lecture_id " +
                	 "JOIN professor p ON p.professor_id = l.professor_id " +
                	 "WHERE lh.student_id = ? AND l.lecture_year = ? AND l.lecture_semester = ?";
		
		ArrayList<LectureDTO> list = new ArrayList<LectureDTO>();
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, studentId);
			pstmt.setString(3, targetYear);
			pstmt.setInt(4, targetSemester);
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					LectureDTO dto = new LectureDTO();
					dto.setLectureId(rs.getInt("lecture_id"));
	                dto.setLectureCode(rs.getInt("lecture_code"));
	                dto.setLectureName(rs.getString("lecture_name"));
	                dto.setProfessorName(rs.getString("professor_name"));
	                
	                // 0보다 크면 평가 완료(true), 아니면 미완료(false)
	                dto.setEvaluated(rs.getInt("is_evaluated") > 0);
					list.add(dto);
				}
			}
		} catch (Exception e) {
			System.out.println("getLectureList() 예외 발생: " + e);
		} return list;
		
	}

	public int writeEvaluation(LectureEvaluationDTO evaDTO) {
		String sql = "INSERT INTO lecture_evaluation (rating, comment, student_id, lecture_id) " +
					 "VALUES (?, ?, ?, ?)";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, evaDTO.getRating());
			pstmt.setString(2, evaDTO.getComment());
			pstmt.setInt(3, evaDTO.getStudentId());
			pstmt.setInt(4, evaDTO.getLectureId());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("writeEvaluation() 예외 발생: " + e);
		} return 0;
	}
	
}
